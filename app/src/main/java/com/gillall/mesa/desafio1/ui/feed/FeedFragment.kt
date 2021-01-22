package com.gillall.mesa.desafio1.ui.feed

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gillall.mesa.desafio1.R

class FeedFragment : Fragment() {

    private var userScroll: Boolean = false
    private lateinit var bundle: Bundle
    private lateinit var feedBinding: com.gillall.mesa.desafio1.databinding.FeedFragmentBinding

    private lateinit var viewModel: FeedViewModel
    private lateinit var viewModelFactory: FeedViewModelFactory

    private lateinit var navController: NavController

    private lateinit var highlightAdapter: HighLightsAdapter

    private lateinit var scrollHandler: Handler


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        feedBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.feed_fragment,
            container,
            false
        )

        navController = NavHostFragment.findNavController(this)

        viewModelFactory = FeedViewModelFactory(
            requireActivity().application,
            arguments?.getString("token").toString()
        )
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(FeedViewModel::class.java)

        feedBinding.lifecycleOwner = this

        setupHighLigths()
        setupNews()

        return feedBinding.root
    }

    private fun setupHighLigths() {
        highlightAdapter = HighLightsAdapter(HighlightListener { highlight, view ->
            if (view.tag == "open") {
                bundle = bundleOf("news" to highlight)
                navController.navigate(R.id.action_feedFragment_to_detailsFragment, bundle)
            } else {
                viewModel.highlightClicked(highlight)
            }
        })
        feedBinding.highlightList.adapter = highlightAdapter
        feedBinding.highlightList.layoutManager =
            GridLayoutManager(activity, 1, GridLayoutManager.HORIZONTAL, false)

        viewModel.newsAndHighlights.observe(viewLifecycleOwner, { data ->
            highlightAdapter.submitList(data.filter { it.highlight })
        })

        feedBinding.highlightList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if(newState == 1) userScroll = true
            }
        })
        autoScroll()

        feedBinding.imgSearch.setOnClickListener {
            navController.navigate(R.id.action_feedFragment_to_filtersFragment)
        }
    }

    private fun setupNews() {
        val newsAdapter = NewsAdapter(NewsListener { news, view ->
            if (view.tag == "open") {
                bundle = bundleOf("news" to news)
                navController.navigate(R.id.action_feedFragment_to_detailsFragment, bundle)
            } else {
                viewModel.highlightClicked(news)
            }
        })
        feedBinding.newsList.adapter = newsAdapter
        feedBinding.newsList.layoutManager =
            GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)

        viewModel.newsAndHighlights.observe(viewLifecycleOwner, { data ->
            val filter = data.filter { !it.highlight }.sortedBy { it.published_at }
            newsAdapter.submitList(filter)
        })
    }

    private fun autoScroll() {//has bugs, need more work
        val speedScroll: Long = 5000
        scrollHandler = Handler()
        var count = 0
        val runnable: Runnable = object : Runnable {
            override fun run() {
                ++count
                if (count > highlightAdapter.itemCount) count = 0
                if (count <= highlightAdapter.itemCount) {
                    feedBinding.highlightList.smoothScrollToPosition(count)
                   if(!userScroll) scrollHandler.postDelayed(this, speedScroll)
                }
            }
        }
        scrollHandler.postDelayed(runnable, speedScroll)
    }
}