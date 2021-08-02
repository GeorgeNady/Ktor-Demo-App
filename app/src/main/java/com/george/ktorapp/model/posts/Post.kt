package com.george.ktorapp.model.posts

data class Post(
    val content: String,
    val created_at: String,
    val dislike_count: Int,
    val dislike_users: List<User>,
    val id: String,
    val my_react: String,
    val likes_count: Int,
    val likes_users: List<User>,
    val modified_at: String,
    val user: User
)