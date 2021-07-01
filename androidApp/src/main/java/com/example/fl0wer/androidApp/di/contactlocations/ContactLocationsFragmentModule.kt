package com.example.fl0wer.androidApp.di.contactlocations

import androidx.lifecycle.ViewModel
import com.example.fl0wer.androidApp.di.core.ViewModelKey
import com.example.fl0wer.androidApp.ui.contactlocations.ContactLocationsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(includes = [ContactLocationsFragmentModule.ViewModelModule::class])
class ContactLocationsFragmentModule {

    @Module
    interface ViewModelModule {
        @[Binds IntoMap ViewModelKey(ContactLocationsViewModel::class)]
        fun provideContactLocationsViewModel(viewModel: ContactLocationsViewModel): ViewModel
    }
}
