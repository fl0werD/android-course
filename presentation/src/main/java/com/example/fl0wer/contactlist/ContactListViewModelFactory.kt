package com.example.fl0wer.contactlist

import dagger.assisted.AssistedFactory

@AssistedFactory
interface ContactListViewModelFactory {
    fun create(): ContactListViewModel
}
