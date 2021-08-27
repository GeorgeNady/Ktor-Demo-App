package com.george.ktorapp.ui.activities.mainActivity.fragments

import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.george.ktorapp.databinding.FragmentSettingBinding
import com.george.ktorapp.ui.base.ActivityFragmentAnnoation
import com.george.ktorapp.ui.base.BaseFragment
import com.george.ktorapp.ui.viewmodel.fragmentsViewModels.SettingFragmentViewModel
import com.george.ktorapp.utiles.Preferences.Companion.prefs
import com.george.ktorapp.utiles.Routes.SETTING_ROUTE

@ActivityFragmentAnnoation(SETTING_ROUTE)
class SettingFragment : BaseFragment<FragmentSettingBinding>() {

    override val TAG: String get() = this.javaClass.name
    lateinit var viewModel: SettingFragmentViewModel

    override fun initialization() {

    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(this).get(SettingFragmentViewModel::class.java)
    }

    override fun setListener() {
        binding?.apply {
            tvDomain.text = prefs.prefsDomain
            tvPort.text = prefs.prefsPort
        }
        binding?.apply {
            btnEditDomain.setOnClickListener {
                toggleVisibility(domainEditSection)
            }
            btnSetDomain.setOnClickListener {
                val domain = etDomain.text.toString()
                prefs.prefsDomain = domain
                tvDomain.text = prefs.prefsDomain
            }
        }
        binding?.apply {
            btnEditPort.setOnClickListener {
                toggleVisibility(portEditSection)
            }
            btnSetPort.setOnClickListener {
                val port = etPort.text.toString()
                prefs.prefsPort = port
                tvPort.text = prefs.prefsPort
            }
        }
    }

    private fun toggleVisibility(view:View) {
        if (view.visibility == View.GONE) view.visibility = View.VISIBLE
        else view.visibility = View.GONE
    }

}