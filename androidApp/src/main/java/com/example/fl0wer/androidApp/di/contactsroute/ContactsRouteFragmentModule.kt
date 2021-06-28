package com.example.fl0wer.androidApp.di.contactsroute

import androidx.lifecycle.ViewModel
import com.example.fl0wer.androidApp.di.core.ViewModelKey
import com.example.fl0wer.androidApp.ui.contactsroute.ContactsRouteFragment
import com.example.fl0wer.androidApp.ui.contactsroute.ContactsRouteScreenParams
import com.example.fl0wer.androidApp.ui.contactsroute.ContactsRouteViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module(includes = [ContactsRouteFragmentModule.ViewModelModule::class])
class ContactsRouteFragmentModule {
    @Provides
    fun provideInitialArgs(fragment: ContactsRouteFragment): ContactsRouteScreenParams =
        fragment.initialArguments()

    @Module
    interface ViewModelModule {
        @[Binds IntoMap ViewModelKey(ContactsRouteViewModel::class)]
        fun provideContactsRouteViewModel(viewModel: ContactsRouteViewModel): ViewModel
    }
}
