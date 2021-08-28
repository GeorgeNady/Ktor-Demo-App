package com.george.ktorapp.viewmodel.fragmentsViewModels

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
import com.george.ktorapp.model.posts.react.ReactRequest
import com.george.ktorapp.network.ApiClient.Companion.api
import com.george.ktorapp.utiles.Preferences.Companion.prefs
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.HttpException

class MainFragmentViewModel(val app: Application) : AndroidViewModel(app) {

    companion object {
        const val TAG = "MainFragmentViewModel"
    }

    private lateinit var insertPostResponseLiveData: MutableLiveData<InsDelPostResponse>
    private lateinit var postsResponseLiveData: MutableLiveData<GetPostsResponse>
    private lateinit var deletePostResponseLiveData: MutableLiveData<InsDelPostResponse>
    private lateinit var reactPostResponseLiveData: MutableLiveData<InsDelPostResponse>

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////// NETWORK CALL /////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    fun createPost(
        postRequest: CreatePostRequest,
        progressBar: ProgressBar,
        button: ImageView
    ): LiveData<InsDelPostResponse> {
        insertPostResponseLiveData = MutableLiveData<InsDelPostResponse>()
        prepareCreatePostResponse(postRequest, progressBar, button)
        return insertPostResponseLiveData
    }

    fun getPosts(
        page: Int,
        progressBar: ProgressBar
    ): LiveData<GetPostsResponse> {
        postsResponseLiveData = MutableLiveData<GetPostsResponse>()
        prepareGetPostsResponse(page, progressBar)
        return postsResponseLiveData
    }

    fun deletePost(
        postId: String,
        progressBar: ProgressBar?
    ): LiveData<InsDelPostResponse> {
        deletePostResponseLiveData = MutableLiveData<InsDelPostResponse>()
        prepareDeletePostResponse(postId, progressBar)
        return deletePostResponseLiveData
    }

    fun react(
        postId: String,
        react: ReactRequest,
        progressBar: ProgressBar,
        imageView: ImageView
    ): LiveData<InsDelPostResponse> {
        reactPostResponseLiveData = MutableLiveData<InsDelPostResponse>()
        prepareReactPostResponse(postId, react, progressBar, imageView)
        return reactPostResponseLiveData
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
                insertPostResponseLiveData.value = postResponse
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

    private fun prepareGetPostsResponse(
        page: Int,
        progressBar: ProgressBar
    ) {
        progressBar.visibility = View.VISIBLE
        val postsResponseObservable = api.getPosts(page, prefs.prefsToken)
            .subscribeOn(Schedulers.single())
            .observeOn(AndroidSchedulers.mainThread())
        val postsResponseObserver = object : Observer<GetPostsResponse> {
            override fun onSubscribe(d: Disposable?) {}
            override fun onNext(postsResponse: GetPostsResponse?) {
                postsResponseLiveData.value = postsResponse
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
        postsResponseObservable.subscribe(postsResponseObserver)
    }

    private fun prepareDeletePostResponse(
        postId:String,
        progressBar: ProgressBar?
    ) {
        progressBar?.visibility = View.VISIBLE

        val observable = api.deletePost(postId, prefs.prefsToken)
            .subscribeOn(Schedulers.single())
            .observeOn(AndroidSchedulers.mainThread())

        val observer = object :  Observer<InsDelPostResponse> {
            override fun onSubscribe(d: Disposable?) {}
            override fun onNext(response: InsDelPostResponse?) {
                deletePostResponseLiveData.value = response
                Log.i(LoginFragmentViewModel.TAG, "onNext: ${response?.message}")
            }

            override fun onError(e: Throwable?) {
                progressBar?.visibility = View.GONE
                Log.e(TAG, "onError: ${e?.message}")
                Log.e(TAG, "onError: ${e}")

                Toast.makeText(app,"you are not authorized to delete this post", Toast.LENGTH_LONG).show()
                
                val error = e as HttpException
                
                when {
                    error.code() == 400  -> {
                        Toast.makeText(app,"you are not authorized to delete this post", Toast.LENGTH_LONG).show()
                        Log.e(TAG, "onError: ${error.message()}" )
                        Log.e(TAG, "onError: ${error.response()}" )
                        Log.e(TAG, "onError: ${error}" )
                    }
                    error.code() == 404 -> {

                    }
                }
            }

            override fun onComplete() {
                progressBar?.visibility = View.GONE
            }
        }
        observable.subscribe(observer)
    }

    private fun prepareReactPostResponse(
        postId:String,
        react : ReactRequest,
        progressBar: ProgressBar?,
        imageView: ImageView?
    ) {
        progressBar?.visibility = View.VISIBLE
        imageView?.visibility = View.GONE

        val observable = api.react(postId, react,prefs.prefsToken)
            .subscribeOn(Schedulers.single())
            .observeOn(AndroidSchedulers.mainThread())

        val observer = object :  Observer<InsDelPostResponse> {
            override fun onSubscribe(d: Disposable?) {}
            override fun onNext(response: InsDelPostResponse?) {
                reactPostResponseLiveData.value = response
                Log.i(LoginFragmentViewModel.TAG, "onNext: ${response?.message}")
            }

            override fun onError(e: Throwable?) {
                progressBar?.visibility = View.GONE
                imageView?.visibility = View.VISIBLE

                Toast.makeText(app,"something went wrong", Toast.LENGTH_LONG).show()

                val error = e as HttpException

                when {
                    error.code() == 400  -> {
                        Toast.makeText(app,"you are not authorized to delete this post", Toast.LENGTH_LONG).show()
                        Log.e(TAG, "onError: ${error.message()}" )
                        Log.e(TAG, "onError: ${error.response()}" )
                        Log.e(TAG, "onError: ${error}" )
                    }
                    error.code() == 404 -> {

                    }
                }
            }

            override fun onComplete() {
                progressBar?.visibility = View.GONE
                imageView?.visibility = View.VISIBLE
            }
        }
        observable.subscribe(observer)
    }
}
