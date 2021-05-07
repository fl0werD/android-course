package com.example.fl0wer.presentation.contactlist

import dagger.assisted.AssistedFactory

@AssistedFactory
interface ContactListViewModelFactory {
    fun create(): ContactListViewModel
}
