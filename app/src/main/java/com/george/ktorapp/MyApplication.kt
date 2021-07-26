package com.george.ktorapp

import android.app.Application
import android.util.Log

class MyApplication: Application() {

    companion object {
        lateinit var instance: MyApplication
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

}