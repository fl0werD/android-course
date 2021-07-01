package com.example.fl0wer.androidApp.ui.contactlist

import com.example.fl0wer.androidApp.data.contacts.ContactMapper.toParcelable
import com.example.fl0wer.androidApp.domain.contacts.Contact
import com.example.fl0wer.androidApp.domain.contacts.ContactsInteractor
import com.example.fl0wer.androidApp.ui.contactlist.adapter.ContactListItem
import com.example.fl0wer.androidApp.ui.core.BaseViewModel
import com.example.fl0wer.androidApp.ui.core.navigation.Screens
import com.example.fl0wer.androidApp.ui.nullOr
import com.github.terrakok.modo.Modo
import com.github.terrakok.modo.forward
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.launch
import timber.log.Timber

class ContactListViewModel @Inject constructor(
    private val modo: Modo,
    private val contactsInteractor: ContactsInteractor,
) : BaseViewModel<ContactListState>(
    initState = ContactListState.Loading,
    modo,
) {
    fun contactClicked(position: Int) {
        val currentState = uiState.nullOr<ContactListState.Idle>() ?: return
        val contactList = (currentState.contacts[position] as? ContactListItem.Contact) ?: return
        modo.forward(Screens.ContactDetails(contactList.contact.lookupKey))
    }

    fun swipeRefresh() {
        uiState.nullOr<ContactListState.Idle>() ?: return
        loadContacts(true)
    }

    fun contactPinsClicked() {
        uiState.nullOr<ContactListState.Idle>() ?: return
        modo.forward(Screens.ContactLocations())
    }

    fun searchTextChanged(nameFilter: String) {
        uiState.nullOr<ContactListState.Idle>() ?: return
        searchContacts(nameFilter)
    }

    fun loadContacts(reload: Boolean = false) {
        vmScope.launch {
            updateState(ContactListState.Loading)
            try {
                val contacts = contactsInteractor.contacts(reload)
                updateState(
                    ContactListState.Idle(
                        contacts.toListItems(),
                    )
                )
            } catch (e: IOException) {
                Timber.e(e)
                updateState(ContactListState.Failure)
            }
        }
    }

    private fun searchContacts(nameFilter: String) {
        vmScope.launch {
            updateState(ContactListState.Loading)
            try {
                val contacts = contactsInteractor.search(nameFilter)
                updateState(
                    ContactListState.Idle(
                        contacts.toListItems(),
                    )
                )
            } catch (e: IOException) {
                Timber.e(e)
                updateState(ContactListState.Failure)
            }
        }
    }

    private fun List<Contact>.toListItems() = map {
        ContactListItem.Contact(it.toParcelable())
    } + listOf(
        ContactListItem.Footer(size)
    )
}
