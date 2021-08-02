package com.george.ktorapp.model.posts

import com.google.gson.annotations.SerializedName

data class InsDelPostResponse(
    @SerializedName("data") val post: Post,
    @SerializedName("message") val message: String,
    @SerializedName("success") val success: Boolean
)