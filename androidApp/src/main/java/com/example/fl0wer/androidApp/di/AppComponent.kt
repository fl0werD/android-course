package com.example.fl0wer.androidApp.di

import android.content.Context
import com.example.fl0wer.androidApp.di.core.ViewModelFactoryModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        AppModule::class,
        ViewModelFactoryModule::class,
        ActivityBindingModule::class,
        DatabaseModule::class,
        NetworkModule::class,
        DataModule::class,
        NavigationModule::class,
    ]
)
interface AppComponent : AndroidInjector<DaggerApplication> {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }
}
