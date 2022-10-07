package com.master.sr.app

import android.app.Application
import android.content.Context
import kotlin.properties.Delegates

class App : Application() {
    companion object {
        var context: Context by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        context = this
    }
}