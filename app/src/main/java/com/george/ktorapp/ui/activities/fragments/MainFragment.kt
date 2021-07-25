package com.george.ktorapp.ui.activities.fragments

import android.annotation.SuppressLint
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.george.ktorapp.databinding.FragmentMainBinding
import com.george.ktorapp.R
import com.george.ktorapp.ui.base.ActivityFragmentAnnoation
import com.george.ktorapp.ui.base.BaseFragment
import com.george.ktorapp.ui.viewmodel.MainFragmentViewModel

@SuppressLint("NonConstantResourceId")
@ActivityFragmentAnnoation(contentId = R.layout.fragment_main)
class MainFragment : BaseFragment<FragmentMainBinding>() {

    override val TAG: String get() = this.javaClass.name
    private lateinit var viewModel: MainFragmentViewModel

    override fun initialization() {

    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(this).get(MainFragmentViewModel::class.java)
    }

    override fun setListener() {
        binding?.apply {
            progressPagination.visibility = View.VISIBLE
        }
    }


}

