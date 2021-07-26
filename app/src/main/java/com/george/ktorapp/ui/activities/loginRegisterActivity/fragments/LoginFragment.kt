package com.george.ktorapp.ui.activities.loginRegisterActivity.fragments

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.george.Models.Person.AuthRequests.LoginRequest
import com.george.ktorapp.R
import com.george.ktorapp.databinding.FragmentLoginBinding
import com.george.ktorapp.model.Auth.RegisterRequest
import com.george.ktorapp.ui.base.ActivityFragmentAnnoation
import com.george.ktorapp.ui.base.BaseFragment
import com.george.ktorapp.ui.viewmodel.fragmentsViewModels.LoginFragmentViewModel

@SuppressLint("NonConstantResourceId")
@ActivityFragmentAnnoation(contentId = R.layout.fragment_login)
class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    override val TAG: String get() = this.javaClass.name
    private lateinit var viewModel: LoginFragmentViewModel

    override fun initialization() {

    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(this).get(LoginFragmentViewModel::class.java)
    }

    override fun setListener() {
        binding?.apply {
            btnSetting.setOnClickListener {
                findNavController().navigate(R.id.settingFragment)
            }
            btnLogin.setOnClickListener {
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()
                val loginRequest = LoginRequest(email,password)
                viewModel.login(loginRequest,progressBar,btnLogin).observe(this@LoginFragment,{ res ->
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
            btnSignup.setOnClickListener {
                findNavController().navigate(
                    R.id.registerFragment,
                    null,
                    navOptions {
                        anim {
                            enter = R.anim.slide_in_right
                            exit = R.anim.slide_out_left
                            popEnter = R.anim.slid_in_left
                            popExit = R.anim.slide_out_right
                        }
                    }
                )
            }
        }
    }
}