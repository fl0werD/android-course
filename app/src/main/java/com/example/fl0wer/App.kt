package com.example.fl0wer

import android.app.Application
import com.example.fl0wer.di.app.AppComponent
import com.example.fl0wer.di.app.DaggerAppComponent
import com.github.terrakok.modo.Modo
import com.github.terrakok.modo.android.AppReducer
import com.github.terrakok.modo.android.LogReducer
import timber.log.Timber

class App : Application() {
    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            appComponent = DaggerAppComponent.factory().create(applicationContext)
        }
    }
}
