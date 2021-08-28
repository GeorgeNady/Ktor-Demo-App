package com.george.ktorapp.base

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.navOptions
import com.george.ktorapp.R
import com.george.ktorapp.utiles.Preferences.Companion.prefs
import com.google.android.material.snackbar.Snackbar
import com.volokh.danylo.hashtaghelper.HashTagHelper


abstract class BaseFragment<T : ViewDataBinding?> : Fragment() {

    abstract val TAG: String

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

    val navOptions = navOptions {
        anim {
            enter = R.anim.slide_in_right
            exit = R.anim.slide_out_left
            popEnter = R.anim.slid_in_left
            popExit = R.anim.slide_out_right
        }
    }

    protected abstract fun initialization() // TODO : add declarations and variables
    protected abstract fun initViewModel() // TODO : add viewModel declaration
    protected abstract fun setListener() // TODO : Logic here

    fun showSnackBar(view:View,message:String) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
    }

    fun clearPrefsUserData() {
        prefs.prefsToken = ""
        prefs.prefsUserEmail = ""
        prefs.prefsUserName = ""
        prefs.prefsUserPhone = ""
    }

    open fun showSnackBar (context: Context,view:View,message:String) {
        Snackbar.make(context,view,message,Snackbar.LENGTH_LONG).show()
    }

    fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
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

    /*open fun dismissP rogressDialog() {
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