package com.george.ktorapp.ui.activities.mainActivity

import android.content.Intent
import com.george.ktorapp.databinding.ActivityMainBinding
import com.george.ktorapp.ui.activities.loginRegisterActivity.LoginRegisterActivity
import com.george.ktorapp.ui.base.BaseActivity
import com.george.ktorapp.ui.viewmodel.MainViewModel

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(
    ActivityMainBinding::inflate
) {

    override val TAG: String get() = this.javaClass.name

    override fun initialization() {
        if (token.isNullOrEmpty()) {
            val intent = Intent(this, LoginRegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun setListener() {

    }

}