package com.example.fl0wer.androidApp.di.main

import androidx.lifecycle.ViewModel
import com.example.fl0wer.androidApp.di.core.ViewModelKey
import com.example.fl0wer.androidApp.ui.main.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(includes = [MainActivityModule.ViewModelModule::class])
class MainActivityModule {

    @Module
    interface ViewModelModule {
        @[Binds IntoMap ViewModelKey(MainViewModel::class)]
        fun bindViewModel(viewModel: MainViewModel): ViewModel
    }
}
