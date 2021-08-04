package com.george.ktorapp.model.posts.react

import com.google.gson.annotations.SerializedName

data class ReactRequest(
    @SerializedName("my_react") val my_react:String
)