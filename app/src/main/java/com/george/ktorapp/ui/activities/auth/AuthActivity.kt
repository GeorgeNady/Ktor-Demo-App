package com.george.ktorapp.ui.activities.auth

import com.george.ktorapp.databinding.ActivityAuthBinding
import com.george.ktorapp.base.BaseActivity
import com.george.ktorapp.viewmodel.LoginRegisterViewModel

class AuthActivity : BaseActivity<ActivityAuthBinding, LoginRegisterViewModel>(
    ActivityAuthBinding::inflate
) {

    override val TAG: String get() = this.javaClass.name

    override fun initialization() {

    }

    override fun setListener() {
        binding.apply {

        }
    }
}