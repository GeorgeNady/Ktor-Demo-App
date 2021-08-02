package com.george.ktorapp.ui.activities.mainActivity.fragments

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import android.widget.AbsListView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.george.ktorapp.R
import com.george.ktorapp.adapters.MyPostsAdapter
import com.george.ktorapp.databinding.FragmentMyPostsBinding
import com.george.ktorapp.model.posts.CreatePostRequest
import com.george.ktorapp.model.posts.InsDelPostResponse
import com.george.ktorapp.model.posts.Post
import com.george.ktorapp.ui.base.ActivityFragmentAnnoation
import com.george.ktorapp.ui.base.BaseFragment
import com.george.ktorapp.ui.viewmodel.fragmentsViewModels.MainFragmentViewModel
import com.george.ktorapp.ui.viewmodel.fragmentsViewModels.MyPostsFragmentViewModel

@SuppressLint("NonConstantResourceId")
@ActivityFragmentAnnoation(contentId = R.layout.fragment_my_posts)
class MyPostsFragment : BaseFragment<FragmentMyPostsBinding>() {

    override val TAG: String get() = this.javaClass.name
    private lateinit var viewModel: MyPostsFragmentViewModel
    private lateinit var myPostsAdapter: MyPostsAdapter
    lateinit var mainViewModel : MainFragmentViewModel
    var isLoading = false
    var isLastPage = false
    var isScrolling = false
    private val myPostsList = mutableListOf<Post>()
    private var myPostsPage = 1

    override fun initialization() {}

    override fun initViewModel() {
        viewModel = ViewModelProvider(this).get(MyPostsFragmentViewModel::class.java)
        mainViewModel = ViewModelProvider(this).get(MainFragmentViewModel::class.java)
        setupRecyclerView()
    }

    override fun setListener() {
        myPostsList.clear()
        viewModel.getMyPosts(myPostsPage, binding!!.progressRecycler)
            .observe(this, { response ->
                binding?.apply {
                    notifyChanges(response.data,myPostsList,tvEmptyList)
                }
            })

        binding?.apply {
            tvEmptyList.visibility = if (myPostsList.isEmpty()) View.VISIBLE else View.GONE
            swipeRefresh.also {
                val primaryColor = resources.getColor(R.color.primary)
                it.setColorSchemeColors(primaryColor)
            }
            swipeRefresh.setOnRefreshListener {
                myPostsPage = 1
                myPostsList.clear()
                isLastPage = false
                viewModel.getMyPosts(myPostsPage, binding!!.progressRecycler)
                    .observe(this@MyPostsFragment, { response ->
                        binding?.apply {
                            notifyChanges(response.data,myPostsList,tvEmptyList)
                        }
                    })
                swipeRefresh.isRefreshing = false
            }
            btnBack.setOnClickListener {
                findNavController().popBackStack()
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
                            .observe(this@MyPostsFragment, { response ->
                                createNewPostHandler(response)
                            })
                    }
                }

            }

        }
    }

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
                myPostsPage++
                viewModel.getMyPosts(myPostsPage, binding!!.progressRecycler)
                    .observe(this@MyPostsFragment, { response ->
                        if (myPostsList.isNotEmpty()) {
                            if (response.data.size < 10) isLastPage = true
                            val newList = response.data.also { Log.d(TAG, "onScrolled: $it") }
                            myPostsList.addAll(newList)
                            myPostsAdapter.differ.submitList(myPostsList).also {
                                Log.d(TAG, "setListener: submitted Paginate")
                                myPostsAdapter.notifyDataSetChanged()
                            }
                            binding!!.tvEmptyList.visibility =
                                if (myPostsList.isEmpty()) View.VISIBLE else View.GONE
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
        myPostsAdapter = MyPostsAdapter(requireContext(),this@MyPostsFragment)
        with(binding!!.rvPosts) {
            itemAnimator = DefaultItemAnimator()
            addOnScrollListener(this@MyPostsFragment.scrollListener)
            adapter = myPostsAdapter
        }
    }

    private fun notifyChanges(posts: List<Post>, viewModelPosts: MutableList<Post>, emptyView: View) {
        viewModelPosts.addAll(posts)
        with(myPostsAdapter) {
            differ.submitList(viewModelPosts).also {
                Log.d(TAG, "notify: $posts")
                myPostsAdapter.notifyDataSetChanged()
            }
        }
        emptyView.visibility = if (viewModelPosts.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun FragmentMyPostsBinding.createNewPostHandler(response: InsDelPostResponse) {
        tvEmptyList.visibility = View.GONE
        showSnackBar(requireContext(), root, response.message)
        etPostContent.text.clear()
        this@MyPostsFragment.hideKeyboard()
        myPostsList.add(0, response.post)
        myPostsAdapter.apply {
            differ.submitList(myPostsList)
            notifyItemInserted(0)
        }
    }

    fun notifyDelete(post:Post,index:Int) {
        try {
            showSnackBar(requireContext(),binding!!.root,"Deleted")
            myPostsList.removeAt(myPostsList.indexOf(post))
            myPostsAdapter.apply {
                differ.submitList(myPostsList)
                notifyItemRemoved(index)
            }
            if (myPostsList.isEmpty()) binding!!.tvEmptyList.visibility = View.GONE
        } catch (e: Exception) {
            Log.e(TAG, "notify: $e", )
        }
    }

}