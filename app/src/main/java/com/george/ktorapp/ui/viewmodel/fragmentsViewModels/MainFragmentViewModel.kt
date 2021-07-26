package com.george.ktorapp.ui.viewmodel.fragmentsViewModels

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


}