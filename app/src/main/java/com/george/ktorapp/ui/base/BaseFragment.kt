package com.george.ktorapp.ui.base

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.george.ktorapp.R
import com.george.ktorapp.ui.activities.fragments.MainFragment
import com.volokh.danylo.hashtaghelper.HashTagHelper


abstract class BaseFragment<T : ViewDataBinding?> : Fragment() {

    abstract val TAG : String

    private var contentId = 0
    protected var bundle: Bundle? = null
    protected var a: Activity? = null
    protected var binding: T? = null
    var progressDialog: ProgressDialog? = null
    var hashTagHelper: HashTagHelper? = null
    var mAlertDialog: AlertDialog? = null

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        if (contentId == 0) {
            bundle = arguments
            contentId = ActivityFragmentAnnoationManager.check(this)
            a = activity
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
         container: ViewGroup?,
         savedInstanceState: Bundle?
    ): View? {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, contentId, container, false)
            initialization()
        }
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        setListener()
    }

    protected abstract fun initialization() // TODO : add declarations and variables
    protected abstract fun initViewModel() // TODO : add viewModel declaration
    protected abstract fun setListener() // TODO : Logic here

    /*open fun showProgressDialog() {
        try {
            if (progressDialog != null && progressDialog!!.isShowing) dismissProgressDialog()
            progressDialog = ProgressDialog(requireContext(), R.style.MyProgressDialogStyle)
            progressDialog!!.setMessage(getString(R.string.please_wait))
            progressDialog!!.window!!.setBackgroundDrawable(
                ColorDrawable(Color.TRANSPARENT)
            )
            progressDialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
            progressDialog!!.show()
            progressDialog!!.setCanceledOnTouchOutside(false)
        } catch (e: Exception) {
        }
    }*/

    /*open fun dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog?.dismiss()
            progressDialog = null
        }
    }*/

    /*open fun dialog(context: Context, message: String?) {
        if (mAlertDialog == null) {
            val alertDialogBuilder = AlertDialog.Builder(context)
            alertDialogBuilder.apply {
                setMessage(message)
                setCancelable(false)
                setTitle("Hint!")
                setPositiveButton("Ok") { arg0: DialogInterface?, arg1: Int ->

                }
                setNegativeButton("Sign In") { dialog, which ->
                    val intent = Intent(context, MainFragment::class.java)
                    context.startActivity(intent)
                }
            }

            mAlertDialog = alertDialogBuilder.create()
        }
        mAlertDialog!!.show()
    }*/
}