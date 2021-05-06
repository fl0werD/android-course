package com.example.fl0wer.di.app

import android.content.Context
import com.example.fl0wer.BuildConfig
import com.github.terrakok.modo.Modo
import com.github.terrakok.modo.android.AppReducer
import com.github.terrakok.modo.android.LogReducer
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object NavigationModule {
    @[Provides Singleton]
    fun bindModo(
        context: Context,
    ): Modo = if (BuildConfig.DEBUG) {
        Modo(LogReducer(AppReducer(context)))
    } else {
        Modo(AppReducer(context))
    }
}
