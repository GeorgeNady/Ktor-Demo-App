package com.george.ktorapp.ui.activities.auth.fragments

import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.george.Models.Person.AuthRequests.LoginRequest
import com.george.ktorapp.R
import com.george.ktorapp.databinding.FragmentLoginBinding
import com.george.ktorapp.ui.activities.main.MainActivity
import com.george.ktorapp.base.ActivityFragmentAnnoation
import com.george.ktorapp.base.BaseFragment
import com.george.ktorapp.viewmodel.fragmentsViewModels.LoginFragmentViewModel
import com.george.ktorapp.utiles.Routes.LOGIN_ROUTE
import kotlin.properties.Delegates

@ActivityFragmentAnnoation(LOGIN_ROUTE)
class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    override val TAG: String get() = this.javaClass.name
    private lateinit var viewModel: LoginFragmentViewModel
    private var canLogin by Delegates.notNull<Boolean>()

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
                val pass = etPassword.text.toString()

                val loginRequest = LoginRequest(email, pass)

                val inputs = mapOf("email" to email, "pass" to pass)
                val inputsRepleteState = mutableListOf<Boolean>()

                inputs.forEach {
                    when(it.key) {
                        "email" -> if (it.value.isEmpty()) etEmail.error = "Missing Email Value"
                        "pass" -> if (it.value.isEmpty()) etPassword.error = "Missing Password Value"
                    }
                }

                inputs.forEach {
                    if (it.value.isEmpty()) inputsRepleteState.add(false) else inputsRepleteState.add(true)
                }

                canLogin = !inputsRepleteState.contains(false)

                Log.d(TAG, "setListener: $canLogin")

                if (canLogin) {
                    viewModel.login(loginRequest,progressBar,btnLogin).observe(this@LoginFragment,{ res ->
                        if (res != null) {
                            val intent = Intent(requireActivity(), MainActivity::class.java)
                            requireActivity().startActivity(intent)
                            requireActivity().finish()
                        }
                    })
                }
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