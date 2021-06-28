package com.example.fl0wer.androidApp.di.contactlist

import androidx.lifecycle.ViewModel
import com.example.fl0wer.androidApp.di.core.ViewModelKey
import com.example.fl0wer.androidApp.ui.contactlist.ContactListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(includes = [ContactListFragmentModule.ViewModelModule::class])
class ContactListFragmentModule {

    @Module
    interface ViewModelModule {
        @[Binds IntoMap ViewModelKey(ContactListViewModel::class)]
        fun provideContactListViewModel(viewModel: ContactListViewModel): ViewModel
    }
}
