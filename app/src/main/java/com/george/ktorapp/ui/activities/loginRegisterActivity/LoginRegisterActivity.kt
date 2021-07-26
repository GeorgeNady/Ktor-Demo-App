package com.george.ktorapp.ui.activities.loginRegisterActivity

import com.george.ktorapp.databinding.ActivityLoginRegisterBinding
import com.george.ktorapp.ui.base.BaseActivity
import com.george.ktorapp.ui.viewmodel.LoginRegisterViewModel

class LoginRegisterActivity : BaseActivity<ActivityLoginRegisterBinding, LoginRegisterViewModel>(
    ActivityLoginRegisterBinding::inflate
) {

    override val TAG: String get() = this.javaClass.name

    override fun initialization() {

    }

    override fun setListener() {
        binding.apply {

        }
    }
}