package com.example.fl0wer.contactdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fl0wer.dispatchers.DispatchersProviderImpl
import com.example.fl0wer.repository.ContactsRepository

class ContactDetailsViewModelFactory(
    private val contactsRepository: ContactsRepository,
    private val param: String,
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContactDetailsViewModel::class.java)) {
            return ContactDetailsViewModel(contactsRepository, DispatchersProviderImpl, param) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
