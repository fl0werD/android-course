package com.example.fl0wer.di.app

import com.example.fl0wer.dispatchers.DispatchersProvider
import com.example.fl0wer.dispatchers.DispatchersProviderImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object AppModule {
    @[Provides Singleton]
    fun provideDispatchersProvider(): DispatchersProvider = DispatchersProviderImpl
}
