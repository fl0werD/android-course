package com.example.fl0wer.androidApp.ui.core.navigation

import com.example.fl0wer.androidApp.ui.contactdetails.ContactDetailsFragment
import com.example.fl0wer.androidApp.ui.contactlist.ContactListFragment
import com.example.fl0wer.androidApp.ui.contactlocations.ContactLocationsFragment
import com.example.fl0wer.androidApp.ui.contactsroute.ContactsRouteFragment
import com.github.terrakok.modo.android.AppScreen
import kotlinx.parcelize.Parcelize

object Screens {
    @Parcelize
    class ContactList : AppScreen("ContactList") {
        override fun create() = ContactListFragment.newInstance()
    }

    @Parcelize
    class ContactDetails(
        private val contactId: String,
    ) : AppScreen("Contact_$contactId") {
        override fun create() = ContactDetailsFragment.newInstance(contactId)
    }

    @Parcelize
    class ContactLocations : AppScreen("ContactLocations") {
        override fun create() = ContactLocationsFragment.newInstance()
    }

    @Parcelize
    class ContactsRoute(
        private val startContact: Int,
        private val endContact: Int,
    ) : AppScreen("Route_${startContact}_${endContact}") {
        override fun create() = ContactsRouteFragment.newInstance(startContact, endContact)
    }
}
