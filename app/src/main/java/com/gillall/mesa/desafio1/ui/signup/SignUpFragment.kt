package com.gillall.mesa.desafio1.ui.signup

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.text.format.Time
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.gillall.mesa.desafio1.R
import com.gillall.mesa.desafio1.databinding.SignupFragmentBinding

class SignUpFragment : Fragment() {

    private lateinit var sigupBinding: SignupFragmentBinding

    private lateinit var viewModel: SignUpViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        sigupBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.signup_fragment,
            container,
            false
        )

        sigupBinding.editTextDate.setOnClickListener {//no time to make this best
            sigupBinding.editTextDate.clearFocus();
            val dpd =
                OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    val s = monthOfYear + 1
                    val a = "$dayOfMonth/$s/$year"
                    sigupBinding.editTextDate.setText("" + a)
                }
            val date = Time()
            val d = DatePickerDialog(requireContext(), dpd, date.year, date.month, date.monthDay)
            d.show()
        }
        return sigupBinding.root
    }

}