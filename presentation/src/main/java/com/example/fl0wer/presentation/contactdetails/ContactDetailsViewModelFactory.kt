package com.example.fl0wer.presentation.contactdetails

import dagger.assisted.AssistedFactory

@AssistedFactory
interface ContactDetailsViewModelFactory {
    fun create(contactId: String): ContactDetailsViewModel
}
