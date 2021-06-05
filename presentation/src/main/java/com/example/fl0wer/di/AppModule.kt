package com.example.fl0wer.di

import com.example.fl0wer.AppScope
import com.example.fl0wer.dispatchers.DispatchersProvider
import com.example.fl0wer.dispatchers.DispatchersProviderImpl
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
