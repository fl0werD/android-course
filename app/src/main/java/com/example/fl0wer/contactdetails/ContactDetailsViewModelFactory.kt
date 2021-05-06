package com.example.fl0wer.contactdetails

import dagger.assisted.AssistedFactory

@AssistedFactory
interface ContactDetailsViewModelFactory {
    fun create(contactId: String): ContactDetailsViewModel
}
