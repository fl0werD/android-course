package com.example.fl0wer.androidApp.ui.contactdetails

import dagger.assisted.AssistedFactory

@AssistedFactory
interface ContactDetailsViewModelFactory {
    fun create(contactId: String): ContactDetailsViewModel
}