package com.example.fl0wer.androidApp.ui.contactlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.fl0wer.androidApp.data.contacts.ContactMapper.toParcelable
import com.example.fl0wer.androidApp.data.contacts.ContactParcelable
import com.example.fl0wer.androidApp.ui.contactlist.adapter.ContactListItem
import com.example.fl0wer.androidApp.ui.core.navigation.Screens
import com.example.fl0wer.androidApp.ui.nullOr
import com.example.fl0wer.domain.contacts.Contact
import com.example.fl0wer.domain.contacts.ContactsInteractor
import com.github.terrakok.modo.Modo
import com.github.terrakok.modo.forward
import dagger.assisted.AssistedInject
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import timber.log.Timber

@Suppress("SwallowedException")
class ContactListViewModel @AssistedInject constructor(
    private val contactsInteractor: ContactsInteractor,
    private val modo: Modo,
) : ViewModel() {
    private val _uiState = MutableStateFlow<ContactListState>(ContactListState.Loading)
    val uiState: StateFlow<ContactListState> get() = _uiState
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
        val contact = currentState.contacts[position].contact
        modo.forward(Screens.ContactDetails(contact.lookupKey))
    }

    fun contactPinsClicked() {
        uiState.value.nullOr<ContactListState.Idle>() ?: return
        modo.forward(Screens.ContactLocations())
    }

    fun contactStateChanged(position: Int) {
        val currentState = uiState.value.nullOr<ContactListState.Idle>() ?: return
        val contact = currentState.contacts[position]
        contact.checked = !contact.checked
        _uiState.value = currentState
    }

    fun searchTextChanged(nameFilter: String) {
        uiState.value.nullOr<ContactListState.Idle>() ?: return
        searchContacts(nameFilter)
    }

    private fun loadContacts() {
        vmScope.launch {
            _uiState.value = ContactListState.Loading
            try {
                val contacts = contactsInteractor.contacts()
                _uiState.value = ContactListState.Idle(contacts.toListItems())
            } catch (e: IOException) {
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
                _uiState.value = ContactListState.Failure
            }
        }
    }

    private fun List<Contact>.toListItems() = map {
        ContactListItem(
            it.toParcelable(),
            false
        )
    }

    companion object {
        fun provideFactory(
            assistedFactory: ContactListViewModelFactory,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create() as T
            }
        }
    }
}
