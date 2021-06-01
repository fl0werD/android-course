package com.example.fl0wer.di

import android.content.Context
import com.example.fl0wer.ContactBroadcastReceiver
import com.example.fl0wer.presentation.contactdetails.ContactDetailsFragment
import com.example.fl0wer.presentation.contactlist.ContactListFragment
import com.example.fl0wer.di.contactdetails.ContactDetailsScope
import com.example.fl0wer.di.contactlist.ContactListScope
import com.example.fl0wer.presentation.main.MainActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        DataModule::class,
        NavigationModule::class,
    ]
)
interface AppComponent {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun inject(contactBroadcastReceiver: ContactBroadcastReceiver)

    fun inject(mainActivity: MainActivity)

    @ContactListScope
    fun inject(contactListFragment: ContactListFragment)

    @ContactDetailsScope
    fun inject(contactDetailsFragment: ContactDetailsFragment)
}
