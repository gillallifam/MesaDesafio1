package com.gillall.mesa.desafio1.ui.filters

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.format.Time
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.GridLayoutManager
import com.gillall.mesa.desafio1.R
import com.gillall.mesa.desafio1.databinding.FiltersFragmentBinding

class FiltersFragment : Fragment() {

    private lateinit var filtersBinding: FiltersFragmentBinding

    private lateinit var viewModel: FiltersViewModel

    private lateinit var bundle: Bundle
    private lateinit var navController: NavController
    private lateinit var  filterAdapter: FiltersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        filtersBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.filters_fragment,
            container,
            false
        )

        viewModel = ViewModelProvider(this).get(FiltersViewModel::class.java)

        viewModel.newsRepository.newsAndHighlights.observe(viewLifecycleOwner, {

        })
        navController = NavHostFragment.findNavController(this)

        setupAdapter()

        //mover para o viewmodel
        filtersBinding.filtSearch.setOnClickListener {
           var filter = viewModel.newsAndHighlights.value!!.filter { it.title.contains(
               filtersBinding.editSearch.text.toString(),
               true
           ) }
            if (filtersBinding.checkFav.isChecked) filter = filter.filter { it.favorite == true }
            filterAdapter.submitList(filter)
        }

        filtersBinding.checkFav.setOnClickListener { it ->
            val btn = it as CheckBox
                var filter = viewModel.newsAndHighlights.value!!.filter { it.favorite == btn.isChecked }
                filterAdapter.submitList(filter)
        }

        /**
         * Works, but ugly.
         */

        filtersBinding.pickDate.setOnClickListener {
            val dpd =
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    val s = monthOfYear + 1
                    val a = "$year-${s.toString().padStart(2,'0')}-${dayOfMonth.toString().padStart(2,'0')}"
                    //val format = SimpleDateFormat("MM/dd/yyyy HH:mm:ss")
                    var filter = viewModel.newsAndHighlights.value!!.filter { it.published_at.split("T")[0] == a  }
                    if (filter.isNotEmpty()) filterAdapter.submitList(filter)
                }
            val date = Time()
            val d =
                DatePickerDialog(requireContext(), dpd, date.year, date.month, date.monthDay)
            d.show()
        }
        return filtersBinding.root
    }

    private fun setupAdapter() {
        filterAdapter = FiltersAdapter(FilterListener { news, view ->
            if (view.tag == "open") {
                bundle = bundleOf("news" to news)
                navController.navigate(R.id.action_filtersFragment_to_detailsFragment, bundle)
            } else {
                //viewModel.highlightClicked(news)
            }
        })
        filtersBinding.filtersList.adapter = filterAdapter
        filtersBinding.filtersList.layoutManager =
            GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)

        viewModel.newsAndHighlights.observe(viewLifecycleOwner, { data ->
            val filter = data.filter { !it.highlight }.sortedBy { it.published_at }
            filterAdapter.submitList(data)
        })
    }
}