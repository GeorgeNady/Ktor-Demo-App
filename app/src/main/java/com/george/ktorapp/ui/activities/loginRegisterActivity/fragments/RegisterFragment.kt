package com.george.ktorapp.ui.activities.loginRegisterActivity.fragments

import android.annotation.SuppressLint
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.george.ktorapp.model.Auth.RegisterRequest
import com.george.ktorapp.R
import com.george.ktorapp.databinding.FragmentRegisterBinding
import com.george.ktorapp.ui.activities.mainActivity.MainActivity
import com.george.ktorapp.ui.base.ActivityFragmentAnnoation
import com.george.ktorapp.ui.base.BaseFragment
import com.george.ktorapp.ui.viewmodel.fragmentsViewModels.RegisterFragmentViewModel
import com.george.ktorapp.utiles.Preferences.Companion.prefs

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
            btnSetting.setOnClickListener {
                findNavController().navigate(R.id.settingFragment)
            }
            btnLogin.setOnClickListener {
                findNavController().popBackStack()
            }
            btnSignup.setOnClickListener {
                val registerRequest = RegisterRequest(
                    username = etName.text.toString(),
                    email = etEmail.text.toString(),
                    password = etPassword.text.toString(),
                    phone = etPhone.text.toString()
                )
                viewModel.register(registerRequest,progressBar,btnSignup).observe(this@RegisterFragment,{ res ->
                    if (res != null) {
                        val intent = Intent(requireActivity(), MainActivity::class.java)
                        requireActivity().startActivity(intent)
                        requireActivity().finish()
                    }
                })
            }
        }
    }
}

