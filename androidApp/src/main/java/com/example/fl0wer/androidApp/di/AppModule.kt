package com.example.fl0wer.androidApp.di

import com.example.fl0wer.androidApp.data.contacts.receiver.AppScope
import com.example.fl0wer.androidApp.ui.core.dispatchers.DispatchersProvider
import com.example.fl0wer.androidApp.ui.core.dispatchers.DispatchersProviderImpl
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
object AppModule {
    @[Provides Singleton]
    fun provideAppScope(): CoroutineScope = AppScope.scope

    @[Provides Singleton]
    fun provideDispatchersProvider(): DispatchersProvider = DispatchersProviderImpl
}
