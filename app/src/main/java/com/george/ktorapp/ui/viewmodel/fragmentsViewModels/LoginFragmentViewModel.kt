package com.george.ktorapp.ui.viewmodel.fragmentsViewModels

import android.app.Application
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.george.Models.Person.AuthRequests.LoginRequest
import com.george.ktorapp.model.Auth.AuthResponse
import com.george.ktorapp.network.ApiClient
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class LoginFragmentViewModel(val app: Application) : AndroidViewModel(app) {

    companion object {
        const val TAG = "LoginFragmentViewModel"
    }

    lateinit var loginResponseLiveData: MutableLiveData<AuthResponse>

    fun login(loginRequest: LoginRequest, progressBar: ProgressBar, button: Button): LiveData<AuthResponse> {
        loginResponseLiveData = MutableLiveData<AuthResponse>()
        prepareLoginResponse(loginRequest, progressBar,button)
        return loginResponseLiveData
    }

    private fun prepareLoginResponse(loginRequest: LoginRequest, progressBar: ProgressBar, button:Button) {
        progressBar.visibility = View.VISIBLE
        button.visibility = View.GONE
        val loginResponseObservable = ApiClient.api.login(loginRequest)
            .subscribeOn(Schedulers.single())
            .observeOn(AndroidSchedulers.mainThread())
        val loginResponseObserver = object : Observer<AuthResponse> {
            override fun onSubscribe(d: Disposable?) {}
            override fun onNext(authResponse: AuthResponse?) {
                loginResponseLiveData.value = authResponse
                Toast.makeText(app,authResponse?.message, Toast.LENGTH_LONG).show()
            }
            override fun onError(e: Throwable?) {
                progressBar.visibility = View.GONE
                button.visibility = View.VISIBLE
                Toast.makeText(app,e?.message, Toast.LENGTH_LONG).show()
            }
            override fun onComplete() {
                progressBar.visibility = View.GONE
                button.visibility = View.VISIBLE
            }
        }
        loginResponseObservable.subscribe(loginResponseObserver)
    }

}