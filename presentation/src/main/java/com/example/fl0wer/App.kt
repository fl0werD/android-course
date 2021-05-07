package com.example.fl0wer

import android.app.Application
import com.example.fl0wer.di.AppComponent
import com.example.fl0wer.di.DaggerAppComponent
import timber.log.Timber

class App : Application() {
    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory().create(applicationContext)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
