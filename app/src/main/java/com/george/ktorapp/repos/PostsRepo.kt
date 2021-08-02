package com.george.ktorapp.repos

import com.george.ktorapp.model.posts.CreatePostRequest
import com.george.ktorapp.model.posts.InsDelPostResponse
import com.george.ktorapp.model.posts.GetPostsResponse
import io.reactivex.rxjava3.core.Observable

abstract class PostsRepo {

    abstract fun createPost(postRequest: CreatePostRequest, token: String): Observable<InsDelPostResponse>

    abstract fun getPosts(page: Int, token: String): Observable<GetPostsResponse>

    abstract fun getMyPosts(page: Int, token: String): Observable<GetPostsResponse>

}