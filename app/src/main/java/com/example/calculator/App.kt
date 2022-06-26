package com.example.calculator

import android.app.Application
import android.content.res.Resources

class App : Application() {

    companion object {
        lateinit var res: Resources
    }

    override fun onCreate() {
        super.onCreate()
        res = resources
    }
}