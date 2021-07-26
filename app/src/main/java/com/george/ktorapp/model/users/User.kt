package com.george.Models.Person.users

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class User(
    @SerializedName("username") @Expose
    val username: String,
    @SerializedName("email") @Expose
    val email: String,
    @SerializedName("password") @Expose
    val password: String,
    @SerializedName("token") @Expose
    val token: String,
    @SerializedName("phone") @Expose
    val phone: String
) : Serializable