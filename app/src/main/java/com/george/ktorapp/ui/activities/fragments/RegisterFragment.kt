package com.george.ktorapp.ui.activities.fragments

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.george.ktorapp.R
import com.george.ktorapp.databinding.FragmentRegisterBinding
import com.george.ktorapp.ui.base.ActivityFragmentAnnoation
import com.george.ktorapp.ui.base.BaseFragment
import com.george.ktorapp.ui.viewmodel.fragmentsViewModels.RegisterFragmentViewModel

@SuppressLint("NonConstantResourceId")
@ActivityFragmentAnnoation(contentId = R.layout.fragment_register)
class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {

    override val TAG: String get() = this.javaClass.name
    private lateinit var viewModel: RegisterFragmentViewModel

    override fun initialization() {

    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(this).get(RegisterFragmentViewModel::class.java)
    }

    override fun setListener() {
        binding?.apply {
            btnLogin.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }
}

