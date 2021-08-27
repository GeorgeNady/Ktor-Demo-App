package com.george.ktorapp.ui.activities.loginRegisterActivity.fragments

import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.george.ktorapp.model.Auth.RegisterRequest
import com.george.ktorapp.R
import com.george.ktorapp.databinding.FragmentRegisterBinding
import com.george.ktorapp.ui.activities.mainActivity.MainActivity
import com.george.ktorapp.ui.base.ActivityFragmentAnnoation
import com.george.ktorapp.ui.base.BaseFragment
import com.george.ktorapp.ui.viewmodel.fragmentsViewModels.RegisterFragmentViewModel
import com.george.ktorapp.utiles.Routes.REGISTER_ROUTE
import kotlin.properties.Delegates

@ActivityFragmentAnnoation(REGISTER_ROUTE)
class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {

    override val TAG: String get() = this.javaClass.name
    private lateinit var viewModel: RegisterFragmentViewModel
    private var canRegister by Delegates.notNull<Boolean>()

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
                val pass = etPassword.text.toString()

                val registerRequest = RegisterRequest(name, email, pass, phone)

                val inputs = mapOf("name" to name, "email" to email , "pass" to pass, "phone" to phone)
                val inputsRepleteState = mutableListOf<Boolean>()

                inputs.forEach {
                    when(it.key) {
                        "name" -> if (it.value.isEmpty()) etName.error = "Missing Name Value"
                        "email" -> if (it.value.isEmpty()) etEmail.error = "Missing Email Value"
                        "pass" -> if (it.value.isEmpty()) etPassword.error = "Missing Password Value"
                        "phone" -> if (it.value.isEmpty()) etPhone.error = "Missing Phone Value"
                    }
                }

                inputs.forEach {
                    if (it.value.isEmpty()) inputsRepleteState.add(false) else inputsRepleteState.add(true)
                }

                canRegister = !inputsRepleteState.contains(false)

                Log.d(TAG, "setListener: $canRegister")

                if (canRegister) {
                    viewModel.register(registerRequest, progressBar, btnSignup)
                        .observe(this@RegisterFragment, { res ->
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
}

