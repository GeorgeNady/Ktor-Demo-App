package com.george.ktorapp.ui.activities.mainActivity.fragments

import android.annotation.SuppressLint
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.george.ktorapp.databinding.FragmentMainBinding
import com.george.ktorapp.R
import com.george.ktorapp.ui.activities.loginRegisterActivity.LoginRegisterActivity
import com.george.ktorapp.ui.base.ActivityFragmentAnnoation
import com.george.ktorapp.ui.base.BaseFragment
import com.george.ktorapp.ui.viewmodel.fragmentsViewModels.MainFragmentViewModel
import com.george.ktorapp.utiles.Preferences.Companion.prefs

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
            tvUserName.text = prefs.prefsUserName
            btnSetting.setOnClickListener {
                findNavController().navigate(R.id.settingFragment)
            }
            btnLogout.setOnClickListener {
                clearPrefsUserData()
                val intent = Intent(requireContext(),LoginRegisterActivity::class.java)
                requireActivity().startActivity(intent)
                requireActivity().finish()
            }
        }
    }

    private fun clearPrefsUserData() {
        prefs.prefsToken = ""
        prefs.prefsUserEmail = ""
        prefs.prefsUserName = ""
        prefs.prefsUserPhone = ""
    }

}

