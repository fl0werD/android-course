package com.example.fl0wer.contactlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fl0wer.CiceroneHolder
import com.example.fl0wer.Contact
import com.example.fl0wer.Screens
import com.example.fl0wer.repository.ContactsRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import timber.log.Timber
import java.io.IOException

class ContactListViewModel(
    private val contactsRepository: ContactsRepository,
) : ViewModel() {
    private val screenState = MutableLiveData<ContactListState>()
    private val exceptionHandler = CoroutineExceptionHandler { _, t ->
        Timber.e(t)
    }
    private val vmScope = viewModelScope + exceptionHandler

    init {
        getContacts()
    }

    fun getScreenState(): LiveData<ContactListState> {
        return screenState
    }

    fun openContact(contact: Contact) {
        CiceroneHolder.router.navigateTo(Screens.contactDetails(contact.lookupKey))
    }

    private fun getContacts() {
        vmScope.launch {
            screenState.value = ContactListState.Loading
            try {
                val contact = contactsRepository.getFirstContact()
                if (contact != null) {
                    screenState.value = ContactListState.Idle(contact)
                } else {
                    screenState.value = ContactListState.Empty
                }
            } catch (e: IOException) {
                screenState.value = ContactListState.Empty
            }
        }
    }
}
