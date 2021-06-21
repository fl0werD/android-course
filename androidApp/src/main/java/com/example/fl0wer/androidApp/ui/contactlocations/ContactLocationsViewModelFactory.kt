package com.example.fl0wer.androidApp.ui.contactlocations

import dagger.assisted.AssistedFactory

@AssistedFactory
interface ContactLocationsViewModelFactory {
    fun create(): ContactLocationsViewModel
}
