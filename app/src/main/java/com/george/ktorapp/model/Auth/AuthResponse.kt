package com.george.ktorapp.model.Auth

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class AuthResponse(
    @SerializedName("success")
    @Expose
    val success:Boolean,
    @SerializedName("message")
    @Expose
    val message:String
) : Serializable