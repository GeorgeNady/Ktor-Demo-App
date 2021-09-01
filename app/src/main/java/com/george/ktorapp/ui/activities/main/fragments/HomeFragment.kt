package com.george.ktorapp.ui.activities.main.fragments

import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.AbsListView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.george.ktorapp.R
import com.george.ktorapp.adapters.PostsAdapter
import com.george.ktorapp.model.posts.CreatePostRequest
import com.george.ktorapp.model.posts.InsDelPostResponse
import com.george.ktorapp.model.posts.Post
import com.george.ktorapp.ui.activities.auth.AuthActivity
import com.george.ktorapp.base.ActivityFragmentAnnoation
import com.george.ktorapp.base.BaseFragment
import com.george.ktorapp.databinding.FragmentHomeBinding
import com.george.ktorapp.viewmodel.fragmentsViewModels.MainFragmentViewModel
import com.george.ktorapp.utiles.Preferences.Companion.prefs
import com.george.ktorapp.utiles.PusherConfiguration.PUSHER_POSTS_CHANNEL
import com.george.ktorapp.utiles.PusherConfiguration.PUSHER_POSTS_EVENT
import com.george.ktorapp.utiles.PusherConfiguration.pusher
import com.george.ktorapp.utiles.Routes.HOME_ROUTE
import com.google.gson.Gson
import com.pusher.client.channel.Channel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@ActivityFragmentAnnoation(HOME_ROUTE)
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    override val TAG: String get() = this.javaClass.name
    lateinit var viewModel: MainFragmentViewModel
    private lateinit var postsAdapter: PostsAdapter
    private lateinit var pusherChannel: Channel

    var isLoading = false
    var isLastPage = false
    var isScrolling = false
    private val postsList = mutableListOf<Post>()
    private var postsPage = 1

    override fun initialization() {
        viewModel = ViewModelProvider(this).get(MainFragmentViewModel::class.java)
        setupRecyclerView()
    }

    override fun setListener() {
        postsList.clear()
        viewModel.getPosts(postsPage, binding!!.progressRecycler)
            .observe(this, { response ->
                binding?.apply {
                    notifyChanges(response.data,postsList,tvEmptyList)
                }
            })

        binding?.apply {
            tvUserName.text = prefs.prefsUserName
            tvEmptyList.visibility = if (postsList.isEmpty()) View.VISIBLE else View.GONE
            swipeRefresh.also {
                val primaryColor = resources.getColor(R.color.primary)
                it.setColorSchemeColors(primaryColor)
            }
            swipeRefresh.setOnRefreshListener {
                postsPage = 1
                postsList.clear()
                isLastPage = false
                viewModel.getPosts(postsPage, binding!!.progressRecycler)
                    .observe(this@HomeFragment, { response ->
                        binding?.apply {
                            notifyChanges(response.data,postsList,tvEmptyList)
                        }
                    })
                swipeRefresh.isRefreshing = false
            }

            btnSetting.setOnClickListener {
                findNavController().navigate(R.id.settingFragment, null, navOptions)
            }
            btnLogout.setOnClickListener {
                clearPrefsUserData()
                val intent = Intent(requireContext(), AuthActivity::class.java)
                requireActivity().startActivity(intent)
                requireActivity().finish()
            }
            btnPost.setOnClickListener {
                val content = CreatePostRequest(etPostContent.text.toString())
                if (content.content.isEmpty()) etPostContent.error =
                    "this is an empty content enter some text and try again"
                else {
                    if (content.content.length < 10) {
                        showSnackBar(requireContext(), root, "I Guess this short content")
                    } else {
                        viewModel.createPost(content, progressSend, btnPost)
                            .observe(this@HomeFragment, { _ ->
                                // createNewPostHandler(response)
                            })
                    }
                }

            }
            ivUserAvatar.setOnClickListener {
                findNavController().navigate(R.id.myProfile2Fragment, null, navOptions)
            }


        }
    }

    // ****************************************************************************** PUSHER HANDLER
    override fun onResume() {
        super.onResume()
        pusher.connect()
        pusherChannel = pusher.subscribe(PUSHER_POSTS_CHANNEL)
        binding?.postsEventBinding()
    }

    override fun onPause() {
        super.onPause()
        pusher.unsubscribe(PUSHER_POSTS_CHANNEL)
    }

    private fun FragmentHomeBinding.postsEventBinding() {
        pusherChannel.bind(PUSHER_POSTS_EVENT) {
            Log.d(TAG, "pusherChannel: ${it.data}")
            val gson = Gson()
            val comment = gson.fromJson(it.data, Post::class.java)
            Log.d(TAG, "postsEventBinding: $comment")
            addNewPostHandler(comment)
        }
    }

    // *********************************************************************** RECYCLER VIEW HANDLER
    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtTheBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= 10
            val shouldPaginate =
                isNotLoadingAndNotLastPage && isAtLastItem && isNotAtTheBeginning &&
                        isTotalMoreThanVisible && isScrolling

            if (shouldPaginate) {
                postsPage++
                viewModel.getPosts(postsPage, binding!!.progressRecycler)
                    .observe(this@HomeFragment, { response ->
                        if (postsList.isNotEmpty()) {
                            if (response.data.size < 10) {
                                isLastPage = true
                                showSnackBar(requireContext(),binding!!.root,"end of result")
                            }
                            notifyChanges(response.data,postsList,binding!!.tvEmptyList)
                        }
                    })
                isScrolling = false
            }

        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }

    private fun setupRecyclerView() {
        postsAdapter = PostsAdapter(requireContext(),this@HomeFragment)
        with(binding!!.rvPosts) {
            itemAnimator = DefaultItemAnimator()
            addOnScrollListener(this@HomeFragment.scrollListener)
            adapter = postsAdapter
        }
    }

    private fun notifyChanges(resPosts: List<Post>, postsList: MutableList<Post>, emptyView: View) {
        try {
            postsList.addAll(resPosts)
            Log.d(TAG, "notify: $resPosts")
            postsAdapter.apply {
                differ.submitList(postsList)
                notifyDataSetChanged()
            }
            emptyView.visibility = if (postsList.isEmpty()) View.VISIBLE else View.GONE
        } catch (e: Exception) {
            Log.e(TAG, "notify: $e", )
        }
    }

    private fun FragmentHomeBinding.createNewPostHandler(response: InsDelPostResponse) {
        tvEmptyList.visibility = View.GONE
        showSnackBar(requireContext(), root, response.message)
        etPostContent.text.clear()
        this@HomeFragment.hideKeyboard()
        postsList.add(0, response.post)
        postsAdapter.apply {
            differ.submitList(postsList)
            notifyItemInserted(0)
        }
    }

    private fun FragmentHomeBinding.addNewPostHandler(post: Post) {
        postsList.add(0, post)
        postsAdapter.apply {
            differ.submitList(postsList)
            CoroutineScope(Dispatchers.Main).launch {
                rvPosts.scrollToPosition(0)
                notifyItemInserted(0)
            }
        }
    }

    fun notifyDelete(post:Post,index:Int) {
        try {
            showSnackBar(requireContext(),binding!!.root,"Deleted")
            postsList.removeAt(postsList.indexOf(post))
            postsAdapter.apply {
                differ.submitList(postsList)
                notifyItemRemoved(index)
            }
            if (postsList.isEmpty()) binding!!.tvEmptyList.visibility = View.GONE
        } catch (e: Exception) {
            Log.e(TAG, "notify: $e", )
        }
    }

}



