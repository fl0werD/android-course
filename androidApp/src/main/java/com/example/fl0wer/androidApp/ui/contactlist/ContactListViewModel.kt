package com.example.fl0wer.androidApp.ui.contactlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fl0wer.androidApp.data.contacts.ContactMapper.toParcelable
import com.example.fl0wer.androidApp.ui.contactlist.adapter.ContactListItem
import com.example.fl0wer.androidApp.ui.core.navigation.Screens
import com.example.fl0wer.androidApp.ui.nullOr
import com.example.fl0wer.domain.contacts.Contact
import com.example.fl0wer.domain.contacts.ContactsInteractor
import com.github.terrakok.modo.Modo
import com.github.terrakok.modo.forward
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class ContactListViewModel @Inject constructor(
    private val contactsInteractor: ContactsInteractor,
    private val modo: Modo,
) : ViewModel() {
    private val _uiState = MutableStateFlow<ContactListState>(ContactListState.Loading)
    val uiState get() = _uiState.asStateFlow()
    private val vmScope = viewModelScope + CoroutineExceptionHandler { _, e ->
        if (e !is CancellationException) {
            Timber.e(e)
        }
    }

    init {
        loadContacts()
    }

    fun contactClicked(position: Int) {
        val currentState = uiState.value.nullOr<ContactListState.Idle>() ?: return
        val contactList = (currentState.contacts[position] as? ContactListItem.Contact) ?: return
        modo.forward(Screens.ContactDetails(contactList.contact.lookupKey))
    }

    fun swipeRefresh() {
        uiState.value.nullOr<ContactListState.Idle>() ?: return
        loadContacts(true)
    }

    fun contactPinsClicked() {
        uiState.value.nullOr<ContactListState.Idle>() ?: return
        modo.forward(Screens.ContactLocations())
    }

    fun searchTextChanged(nameFilter: String) {
        uiState.value.nullOr<ContactListState.Idle>() ?: return
        searchContacts(nameFilter)
    }

    private fun loadContacts(reload: Boolean = false) {
        vmScope.launch {
            _uiState.value = ContactListState.Loading
            try {
                val contacts = contactsInteractor.contacts(reload)
                _uiState.value = ContactListState.Idle(contacts.toListItems())
            } catch (e: IOException) {
                Timber.e(e)
                _uiState.value = ContactListState.Failure
            }
        }
    }

    private fun searchContacts(nameFilter: String) {
        vmScope.launch {
            _uiState.value = ContactListState.Loading
            try {
                val contacts = contactsInteractor.search(nameFilter)
                _uiState.value = ContactListState.Idle(contacts.toListItems())
            } catch (e: IOException) {
                Timber.e(e)
                _uiState.value = ContactListState.Failure
            }
        }
    }

    private fun List<Contact>.toListItems() = map {
        ContactListItem.Contact(it.toParcelable())
    } + listOf(
        ContactListItem.Footer(size)
    )
}
