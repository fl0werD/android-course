package com.example.fl0wer.contactlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fl0wer.dispatchers.DispatchersProviderImpl
import com.example.fl0wer.repository.ContactsRepository

class ContactListViewModelFactory(
    private val contactsRepository: ContactsRepository,
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContactListViewModel::class.java)) {
            return ContactListViewModel(contactsRepository, DispatchersProviderImpl) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
