package com.george.ktorapp.ui.activities.mainActivity.fragments

import android.annotation.SuppressLint
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import com.george.ktorapp.R
import com.george.ktorapp.databinding.FragmentSettingBinding
import com.george.ktorapp.ui.base.ActivityFragmentAnnoation
import com.george.ktorapp.ui.base.BaseFragment
import com.george.ktorapp.ui.viewmodel.fragmentsViewModels.SettingFragmentViewModel
import com.george.ktorapp.utiles.Preferences.Companion.prefs

@SuppressLint("NonConstantResourceId")
@ActivityFragmentAnnoation(contentId = R.layout.fragment_setting)
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