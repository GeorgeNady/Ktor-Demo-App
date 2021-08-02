package com.george.ktorapp.model.posts

data class GetPostsResponse(
    val `data`: List<Post>,
    val message: String,
    val page: Int,
    val success: Boolean,
    val totalResult: Int
)