package com.example.fl0wer.androidApp.di

import com.example.fl0wer.androidApp.di.contactdetails.ContactDetailsFragmentModule
import com.example.fl0wer.androidApp.di.contactdetails.ContactDetailsScope
import com.example.fl0wer.androidApp.di.contactlist.ContactListFragmentModule
import com.example.fl0wer.androidApp.di.contactlist.ContactListScope
import com.example.fl0wer.androidApp.di.contactlocation.ContactLocationFragmentModule
import com.example.fl0wer.androidApp.di.contactlocation.ContactLocationScope
import com.example.fl0wer.androidApp.di.contactlocations.ContactLocationsFragmentModule
import com.example.fl0wer.androidApp.di.contactlocations.ContactLocationsScope
import com.example.fl0wer.androidApp.di.contactsroute.ContactsRouteFragmentModule
import com.example.fl0wer.androidApp.di.contactsroute.ContactsRouteScope
import com.example.fl0wer.androidApp.ui.contactdetails.ContactDetailsFragment
import com.example.fl0wer.androidApp.ui.contactlist.ContactListFragment
import com.example.fl0wer.androidApp.ui.contactlocation.ContactLocationFragment
import com.example.fl0wer.androidApp.ui.contactlocations.ContactLocationsFragment
import com.example.fl0wer.androidApp.ui.contactsroute.ContactsRouteFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface FragmentBindingModule {
    @ContactListScope
    @ContributesAndroidInjector(modules = [ContactListFragmentModule::class])
    fun bindContactListFragment(): ContactListFragment

    @ContactDetailsScope
    @ContributesAndroidInjector(modules = [ContactDetailsFragmentModule::class])
    fun bindContactDetailsFragment(): ContactDetailsFragment

    @ContactLocationScope
    @ContributesAndroidInjector(modules = [ContactLocationFragmentModule::class])
    fun bindContactLocationFragment(): ContactLocationFragment

    @ContactLocationsScope
    @ContributesAndroidInjector(modules = [ContactLocationsFragmentModule::class])
    fun bindContactLocationsFragment(): ContactLocationsFragment

    @ContactsRouteScope
    @ContributesAndroidInjector(modules = [ContactsRouteFragmentModule::class])
    fun bindContactsRouteFragment(): ContactsRouteFragment
}
