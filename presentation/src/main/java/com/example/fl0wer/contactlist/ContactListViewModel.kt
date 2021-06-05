package com.example.fl0wer.contactlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.fl0wer.Screens
import com.example.fl0wer.contacts.ContactsInteractor
import com.example.fl0wer.mapper.ContactMapper
import com.example.fl0wer.nullOr
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
        val contact = currentState.contacts[position]
        modo.forward(Screens.ContactDetails(contact.lookupKey))
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
                _uiState.value = ContactListState.Idle(ContactMapper.map(contacts))
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
                _uiState.value = ContactListState.Idle(ContactMapper.map(contacts))
            } catch (e: IOException) {
                _uiState.value = ContactListState.Failure
            }
        }
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
