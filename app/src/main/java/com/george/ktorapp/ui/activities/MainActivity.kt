package com.george.ktorapp.ui.activities

import com.george.ktorapp.databinding.ActivityMainBinding
import com.george.ktorapp.ui.base.BaseActivity
import com.george.ktorapp.ui.viewmodel.MainViewModel

class MainActivity :
    BaseActivity<ActivityMainBinding, MainViewModel>(ActivityMainBinding::inflate) {

    override val TAG: String get() = this.javaClass.name

    override fun setListener() {
        binding.apply {

        }
    }

}