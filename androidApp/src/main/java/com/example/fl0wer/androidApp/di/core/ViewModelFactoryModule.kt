package com.example.fl0wer.androidApp.di.core

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface ViewModelFactoryModule {
    @[Binds Singleton]
    fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}
