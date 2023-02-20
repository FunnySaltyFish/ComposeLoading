package com.funny.compose.loading

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        appCtx = this
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var appCtx: Context
    }
}

internal fun stringResource(id: Int) = App.appCtx.getString(id)
