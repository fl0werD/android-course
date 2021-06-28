package com.example.fl0wer.androidApp.di.contactlocation

import androidx.lifecycle.ViewModel
import com.example.fl0wer.androidApp.di.core.ViewModelKey
import com.example.fl0wer.androidApp.ui.contactlocation.ContactLocationFragment
import com.example.fl0wer.androidApp.ui.contactlocation.ContactLocationScreenParams
import com.example.fl0wer.androidApp.ui.contactlocation.ContactLocationViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module(includes = [ContactLocationFragmentModule.ViewModelModule::class])
class ContactLocationFragmentModule {
    @Provides
    fun provideInitialArgs(fragment: ContactLocationFragment): ContactLocationScreenParams =
        fragment.initialArguments()

    @Module
    interface ViewModelModule {
        @[Binds IntoMap ViewModelKey(ContactLocationViewModel::class)]
        fun provideContactLocationViewModel(viewModel: ContactLocationViewModel): ViewModel
    }
}
