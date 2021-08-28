package com.george.ktorapp.ui.activities.main

import android.content.Intent
import com.george.ktorapp.databinding.ActivityMainBinding
import com.george.ktorapp.ui.activities.auth.AuthActivity
import com.george.ktorapp.base.BaseActivity
import com.george.ktorapp.viewmodel.MainViewModel

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(
    ActivityMainBinding::inflate
) {

    override val TAG: String get() = this.javaClass.name

    override fun initialization() {
        if (token.isNullOrEmpty()) {
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun setListener() {

    }

}