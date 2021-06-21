package com.example.fl0wer.androidApp.ui.contactlist

import dagger.assisted.AssistedFactory

@AssistedFactory
interface ContactListViewModelFactory {
    fun create(): ContactListViewModel
}
