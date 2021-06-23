package com.example.fl0wer.androidApp.ui.contactsroute

import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory

@AssistedFactory
interface ContactsRouteViewModelFactory {
    fun create(
        @Assisted("startContact") startContact: Int,
        @Assisted("endContact") endContact: Int,
    ): ContactsRouteViewModel
}
