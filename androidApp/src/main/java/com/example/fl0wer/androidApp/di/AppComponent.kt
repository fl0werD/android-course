package com.example.fl0wer.androidApp.di

import android.content.Context
import com.example.fl0wer.androidApp.data.contacts.receiver.ContactBroadcastReceiver
import com.example.fl0wer.androidApp.di.contactdetails.ContactDetailsFragmentModule
import com.example.fl0wer.androidApp.ui.contactdetails.ContactDetailsFragment
import com.example.fl0wer.androidApp.ui.contactlist.ContactListFragment
import com.example.fl0wer.androidApp.di.contactdetails.ContactDetailsScope
import com.example.fl0wer.androidApp.di.contactlist.ContactListFragmentModule
import com.example.fl0wer.androidApp.di.contactlist.ContactListScope
import com.example.fl0wer.androidApp.di.contactlocation.ContactLocationFragmentModule
import com.example.fl0wer.androidApp.di.contactlocation.ContactLocationScope
import com.example.fl0wer.androidApp.di.contactlocations.ContactLocationsFragmentModule
import com.example.fl0wer.androidApp.di.contactlocations.ContactLocationsScope
import com.example.fl0wer.androidApp.di.contactsroute.ContactsRouteFragmentModule
import com.example.fl0wer.androidApp.di.contactsroute.ContactsRouteScope
import com.example.fl0wer.androidApp.di.core.ViewModelFactoryModule
import com.example.fl0wer.androidApp.ui.contactlocation.ContactLocationFragment
import com.example.fl0wer.androidApp.ui.contactlocations.ContactLocationsFragment
import com.example.fl0wer.androidApp.ui.contactsroute.ContactsRouteFragment
import com.example.fl0wer.androidApp.ui.main.MainActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        ViewModelFactoryModule::class,
        ContactListFragmentModule::class,
        ContactDetailsFragmentModule::class,
        ContactLocationFragmentModule::class,
        ContactLocationsFragmentModule::class,
        ContactsRouteFragmentModule::class,
        DatabaseModule::class,
        NetworkModule::class,
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

    @ContactLocationScope
    fun inject(contactLocationFragment: ContactLocationFragment)

    @ContactLocationsScope
    fun inject(contactLocationsFragment: ContactLocationsFragment)

    @ContactsRouteScope
    fun inject(contactsRouteFragment: ContactsRouteFragment)
}
