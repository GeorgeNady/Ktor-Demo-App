package com.george.ktorapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.george.ktorapp.network.ApiClient.Companion.api
import com.george.ktorapp.utiles.Preferences.Companion.prefs
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class MainFragmentViewModel(app: Application) : AndroidViewModel(app) {

    companion object {
        const val TAG = "MainFragmentViewModel"
    }

    private val clashesMutableLiveData: MutableLiveData<Any>? = null

    fun vote(id: String) {
        val clashesObservable: Observable<Any> =
            api.login(id, prefs.prefsToken)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
        val clashesObserver: Observer<Any> = object : Observer<Any> {
            override fun onSubscribe(d: Disposable?) {
                TODO("Not yet implemented")
            }

            override fun onNext(t: Any) {
                TODO("Not yet implemented")
            }

            override fun onError(e: Throwable) {
                TODO("Not yet implemented")
            }

            override fun onComplete() {
                TODO("Not yet implemented")
            }
        }
        clashesObservable.subscribe(clashesObserver)
    }


}