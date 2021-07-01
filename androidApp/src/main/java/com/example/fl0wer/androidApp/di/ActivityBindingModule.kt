package com.example.fl0wer.androidApp.di

import com.example.fl0wer.androidApp.di.main.MainActivityModule
import com.example.fl0wer.androidApp.ui.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ActivityBindingModule {
    @ContributesAndroidInjector(modules = [MainActivityModule::class, FragmentBindingModule::class])
    fun bindMainActivity(): MainActivity
}
