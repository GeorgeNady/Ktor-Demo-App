package com.george.ktorapp.ui.viewmodel.fragmentsViewModels

import android.app.Application
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.george.ktorapp.model.posts.CreatePostRequest
import com.george.ktorapp.model.posts.InsDelPostResponse
import com.george.ktorapp.model.posts.GetPostsResponse
import com.george.ktorapp.model.posts.Post
import com.george.ktorapp.network.ApiClient.Companion.api
import com.george.ktorapp.utiles.Preferences.Companion.prefs
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class MyPostsFragmentViewModel(val app: Application) : AndroidViewModel(app) {

    companion object {
        const val TAG = "MyPostsFragmentViewModel"
    }

    private lateinit var postResponseLiveData: MutableLiveData<InsDelPostResponse>
    private lateinit var myPostsResponseLiveData: MutableLiveData<GetPostsResponse>

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////// NETWORK CALL /////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    fun createPost(
        postRequest: CreatePostRequest,
        progressBar: ProgressBar,
        button: ImageView
    ): LiveData<InsDelPostResponse> {
        postResponseLiveData = MutableLiveData<InsDelPostResponse>()
        prepareCreatePostResponse(postRequest, progressBar, button)
        return postResponseLiveData
    }

    fun getMyPosts(
        page: Int,
        progressBar: ProgressBar
    ): LiveData<GetPostsResponse> {
        myPostsResponseLiveData = MutableLiveData<GetPostsResponse>()
        prepareGetMyPostsResponse(page, progressBar)
        return myPostsResponseLiveData
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////// PREPARING /////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    private fun prepareCreatePostResponse(
        createPostRequest: CreatePostRequest,
        progressBar: ProgressBar,
        button: ImageView
    ) {
        progressBar.visibility = View.VISIBLE
        button.visibility = View.GONE
        val postResponseObservable = api.createPost(createPostRequest, prefs.prefsToken)
            .subscribeOn(Schedulers.single())
            .observeOn(AndroidSchedulers.mainThread())
        val postResponseObserver = object : Observer<InsDelPostResponse> {
            override fun onSubscribe(d: Disposable?) {}
            override fun onNext(postResponse: InsDelPostResponse?) {
                postResponseLiveData.value = postResponse
                Log.i(LoginFragmentViewModel.TAG, "onNext: ${postResponse?.message}")
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
        postResponseObservable.subscribe(postResponseObserver)
    }

    private fun prepareGetMyPostsResponse(
        page: Int,
        progressBar: ProgressBar
    ) {
        progressBar.visibility = View.VISIBLE
        val myPostsResponseObservable = api.getMyPosts(page, prefs.prefsToken)
            .subscribeOn(Schedulers.single())
            .observeOn(AndroidSchedulers.mainThread())
        val myPostsResponseObserver = object : Observer<GetPostsResponse> {
            override fun onSubscribe(d: Disposable?) {}
            override fun onNext(postsResponse: GetPostsResponse?) {
                myPostsResponseLiveData.value = postsResponse
//                Toast.makeText(app,postsResponse?.message, Toast.LENGTH_LONG).show()
                Log.i(LoginFragmentViewModel.TAG, "onNext: ${postsResponse?.message}")
            }

            override fun onError(e: Throwable?) {
                progressBar.visibility = View.GONE
                Toast.makeText(app,e?.message, Toast.LENGTH_LONG).show()
            }

            override fun onComplete() {
                progressBar.visibility = View.GONE
            }
        }
        myPostsResponseObservable.subscribe(myPostsResponseObserver)
    }
}