package com.george.ktorapp.model

import com.google.gson.annotations.SerializedName

data class TestResponse(
    @SerializedName("message") val message: String
)
