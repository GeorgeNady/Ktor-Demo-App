package com.george.ktorapp.ui.activities.loginRegisterActivity.fragments

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.george.ktorapp.model.Auth.RegisterRequest
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
            btnSetting.setOnClickListener {
                findNavController().navigate(R.id.settingFragment)
            }
            btnLogin.setOnClickListener {
                findNavController().popBackStack()
            }
            btnSignup.setOnClickListener {
                val name = etName.text.toString()
                val email = etEmail.text.toString()
                val phone = etPhone.text.toString()
                val password = etPassword.text.toString()
                val registerRequest = RegisterRequest(name,email,password,phone)
                viewModel.register(registerRequest,progressBar,btnSignup).observe(this@RegisterFragment,{ res ->
                    if (res != null) {
                        /*prefs.apply {
                            prefsToken = res.user.token
                            prefsUserName = res.user.username
                            prefsEmail = res.user.email
                            prefsPhone = res.user.phone
                        }*/
                    }
                })
            }
        }
    }
}

