package com.example.fl0wer.androidApp.ui.core.navigation

import com.example.fl0wer.androidApp.ui.contactdetails.ContactDetailsFragment
import com.example.fl0wer.androidApp.ui.contactdetails.ContactDetailsScreenParams
import com.example.fl0wer.androidApp.ui.contactlist.ContactListFragment
import com.example.fl0wer.androidApp.ui.contactlocation.ContactLocationFragment
import com.example.fl0wer.androidApp.ui.contactlocation.ContactLocationScreenParams
import com.example.fl0wer.androidApp.ui.contactlocations.ContactLocationsFragment
import com.example.fl0wer.androidApp.ui.contactsroute.ContactsRouteFragment
import com.example.fl0wer.androidApp.ui.contactsroute.ContactsRouteScreenParams
import com.github.terrakok.modo.android.AppScreen
import kotlinx.parcelize.Parcelize

object Screens {
    @Parcelize
    class ContactList : AppScreen("ContactList") {
        override fun create() = ContactListFragment.newInstance()
    }

    @Parcelize
    class ContactDetails(
        private val contactLookupKey: String,
    ) : AppScreen("Contact_$contactLookupKey") {
        override fun create() = ContactDetailsFragment.newInstance(
            ContactDetailsScreenParams(
                contactLookupKey,
            )
        )
    }

    @Parcelize
    class ContactLocation(
        private val contactId: Int,
    ) : AppScreen("Location_$contactId") {
        override fun create() = ContactLocationFragment.newInstance(
            ContactLocationScreenParams(
                contactId,
            )
        )
    }

    @Parcelize
    class ContactLocations : AppScreen("Locations") {
        override fun create() = ContactLocationsFragment.newInstance()
    }

    @Parcelize
    class ContactsRoute(
        private val startContact: Int,
    ) : AppScreen("Route_$startContact") {
        override fun create() = ContactsRouteFragment.newInstance(
            ContactsRouteScreenParams(
                startContact,
            )
        )
    }
}
