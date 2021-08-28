package com.george.ktorapp.base

import android.app.Application

class BaseApplication: Application() {

    companion object {
        lateinit var instance: BaseApplication
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

}