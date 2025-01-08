package com.example.task37

import android.app.Application
import android.content.Context
import com.example.task37.ioc.ApplicationComponent

class App: Application() {
    val applicationComponent by lazy { ApplicationComponent(applicationContext) }

    companion object {
        /**
         * Shortcut to get [App] instance from any context, e.g. from Activity.
         */
        fun get(context: Context): App = context.applicationContext as App
    }
}