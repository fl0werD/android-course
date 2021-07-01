package com.example.fl0wer.androidApp.di.contactdetails

import androidx.lifecycle.ViewModel
import com.example.fl0wer.androidApp.di.core.FragmentWithArgsModule
import com.example.fl0wer.androidApp.di.core.ViewModelKey
import com.example.fl0wer.androidApp.ui.contactdetails.ContactDetailsFragment
import com.example.fl0wer.androidApp.ui.contactdetails.ContactDetailsScreenParams
import com.example.fl0wer.androidApp.ui.contactdetails.ContactDetailsState
import com.example.fl0wer.androidApp.ui.contactdetails.ContactDetailsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(includes = [ContactDetailsFragmentModule.ViewModelModule::class])
class ContactDetailsFragmentModule :
    FragmentWithArgsModule<ContactDetailsFragment, ContactDetailsState, ContactDetailsScreenParams>() {

    @Module
    interface ViewModelModule {
        @[Binds IntoMap ViewModelKey(ContactDetailsViewModel::class)]
        fun provideContactDetailsViewModel(viewModel: ContactDetailsViewModel): ViewModel
    }
}
