package com.george.ktorapp.adapters

import android.content.Context
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.ProgressBar
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.george.ktorapp.R
import com.george.ktorapp.databinding.ItemPostBinding
import com.george.ktorapp.model.posts.Post
import com.george.ktorapp.ui.activities.main.fragments.MyProfileFragment

class MyPostsAdapter(val context: Context, val owner: MyProfileFragment) :
    RecyclerView.Adapter<MyPostsAdapter.PostsAdapterViewHolder>() {

    inner class PostsAdapterViewHolder(val binding: ItemPostBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallBack = object : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post) = oldItem._id == newItem._id
        override fun areContentsTheSame(oldItem: Post, newItem: Post) = oldItem == newItem
    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PostsAdapterViewHolder(ItemPostBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ))

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: PostsAdapterViewHolder, position: Int) {
        val current = differ.currentList[position]
        holder.binding.apply {
            tvUserName.text = current.user.username
            tvLastUpdate.text = current.modified_at
            tvContent.text = current.content
            tvLikes.text = current.likes_count.toString()
            tvDislikes.text = current.dislike_count.toString()

            when (current.my_react) {
                "like" -> {
                    btnLike.setImageResource(R.drawable.ic_like_on)
                    btnDisLike.setImageResource(R.drawable.ic_dislike_off)
                }
                "dislike" -> {
                    btnLike.setImageResource(R.drawable.ic_like_off)
                    btnDisLike.setImageResource(R.drawable.ic_dislike_on)
                }
                "" -> {
                    btnLike.setImageResource(R.drawable.ic_like_off)
                    btnDisLike.setImageResource(R.drawable.ic_dislike_off)
                }
            }
            ////////////////////////////////////////////////////////////////////////////////////////
            ////////// { DELETE / EDIT } ///////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////////////////////////////////////////
            btnMore.setOnClickListener {
                val popupMenu = PopupMenu(context, btnMore)
                popupMenu.inflate(R.menu.popup_menu)
                popupMenu.setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.miEdit -> {
                            true
                        }
                        R.id.miDelete -> {
                            deletePost(current._id, progressBar /*current*/)
                            true
                        }
                        else -> false
                    }
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    popupMenu.setForceShowIcon(true)
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    popupMenu.gravity = Gravity.RIGHT
                }
                popupMenu.show()
            }

            root.setOnClickListener { onItemClickListener?.let { it(current) } }
        }
    }

    private var onItemClickListener: ((Post) -> Unit)? = null

    fun setOnItemClickListener(listener: (Post) -> Unit) {
        onItemClickListener = listener
    }

    private fun deletePost(postId: String, progressBar: ProgressBar) {
        var postIndex = 0
        var mPost:Post? = null
        for (post in differ.currentList) {
            if (post._id == postId) {
                postIndex = differ.currentList.indexOf(post)
                mPost = differ.currentList[postIndex]!!
            }
        }
        owner.apply {
            mainViewModel.deletePost(postId, progressBar)
                .observe(this, { notifyDelete(mPost!!,postIndex) })
        }
    }

}