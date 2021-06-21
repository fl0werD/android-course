package com.example.fl0wer.androidApp.ui.main

import androidx.lifecycle.ViewModel
import com.example.fl0wer.androidApp.ui.core.navigation.Screens
import com.github.terrakok.modo.Modo
import com.github.terrakok.modo.back
import com.github.terrakok.modo.forward
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val modo: Modo,
) : ViewModel() {
    fun openContact(contactId: String) {
        modo.forward(Screens.ContactDetails(contactId))
    }

    fun backPressed() {
        modo.back()
    }
}
