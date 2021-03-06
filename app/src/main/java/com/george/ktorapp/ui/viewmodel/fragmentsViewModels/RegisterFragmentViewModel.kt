package com.george.ktorapp.ui.viewmodel.fragmentsViewModels

import android.app.Application
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.george.ktorapp.model.Auth.RegisterRequest
import com.george.ktorapp.model.Auth.AuthResponse
import com.george.ktorapp.network.ApiClient
import com.george.ktorapp.utiles.Preferences.Companion.prefs
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class RegisterFragmentViewModel(val app: Application) : AndroidViewModel(app) {

    companion object {
        const val TAG = "MainFragmentViewModel"
    }

    lateinit var registerResponseLiveData: MutableLiveData<AuthResponse>

    fun register(register: RegisterRequest, progressBar: ProgressBar, button:Button): LiveData<AuthResponse> {
        registerResponseLiveData = MutableLiveData<AuthResponse>()
        prepareRegisterResponse(register, progressBar,button)
        return registerResponseLiveData
    }

    private fun prepareRegisterResponse(registerRequest: RegisterRequest, progressBar: ProgressBar, button:Button) {
        progressBar.visibility = View.VISIBLE
        button.visibility = View.GONE
        val registerResponseObservable = ApiClient.api.register(registerRequest)
            .subscribeOn(Schedulers.single())
            .observeOn(AndroidSchedulers.mainThread())
        val registerResponseObserver = object : Observer<AuthResponse> {
            override fun onSubscribe(d: Disposable?) {}
            override fun onNext(authResponse: AuthResponse?) {
                registerResponseLiveData.value = authResponse
                prefs.apply {
                    prefsToken = authResponse!!.message
                    prefsUserId = authResponse.user.id
                    prefsUserName = authResponse.user.username
                    prefsUserEmail = authResponse.user.email
                    prefsUserPhone = authResponse.user.phone
                }
                Toast.makeText(app,authResponse?.message,Toast.LENGTH_LONG).show()
            }
            override fun onError(e: Throwable?) {
                progressBar.visibility = View.GONE
                button.visibility = View.VISIBLE
                Toast.makeText(app,e?.message,Toast.LENGTH_LONG).show()
            }
            override fun onComplete() {
                progressBar.visibility = View.GONE
                button.visibility = View.VISIBLE
            }
        }
        registerResponseObservable.subscribe(registerResponseObserver)
    }


}