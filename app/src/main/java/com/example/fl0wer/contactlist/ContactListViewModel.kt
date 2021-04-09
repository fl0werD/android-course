package com.example.fl0wer.contactlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fl0wer.CiceroneHolder
import com.example.fl0wer.Screens
import com.example.fl0wer.nullOr
import com.example.fl0wer.repository.ContactsRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import timber.log.Timber
import java.io.IOException

class ContactListViewModel(
    private val contactsRepository: ContactsRepository,
) : ViewModel() {
    private val stateLiveData = MutableLiveData<ContactListState>()
    private val state: ContactListState
        get() = stateLiveData.value ?: throw IllegalStateException("state value should not be null")
    private val exceptionHandler = CoroutineExceptionHandler { _, t ->
        Timber.e(t)
    }
    private val vmScope = viewModelScope + exceptionHandler

    init {
        getContacts()
    }

    fun getScreenState(): LiveData<ContactListState> {
        return stateLiveData
    }

    fun contactClicked(position: Int) {
        val currentState = state.nullOr<ContactListState.Idle>() ?: return
        val contact = currentState.contacts[position]
        CiceroneHolder.router.navigateTo(Screens.contactDetails(contact.lookupKey))
    }

    fun searchTextChanged(nameFilter: String) {
        state.nullOr<ContactListState.Idle>() ?: return
        vmScope.launch {
            stateLiveData.value = ContactListState.Loading
            try {
                val contacts = contactsRepository.getSearchedContacts(nameFilter.trim())
                stateLiveData.value = ContactListState.Idle(contacts)
            } catch (e: IOException) {
                stateLiveData.value = ContactListState.Empty
            }
        }
    }

    private fun getContacts() {
        vmScope.launch {
            stateLiveData.value = ContactListState.Loading
            try {
                val contacts = contactsRepository.getContacts()
                stateLiveData.value = ContactListState.Idle(contacts)
            } catch (e: IOException) {
                stateLiveData.value = ContactListState.Empty
            }
        }
    }
}
