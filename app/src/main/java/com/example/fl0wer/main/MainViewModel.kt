package com.example.fl0wer.main

import androidx.lifecycle.ViewModel
import com.example.fl0wer.CiceroneHolder
import com.example.fl0wer.Screens

class MainViewModel : ViewModel() {
    fun startNavigation() {
        CiceroneHolder.router.newRootChain(Screens.contactList())
    }

    fun openContact(contactId: String) {
        CiceroneHolder.router.navigateTo(Screens.contactDetails(contactId))
    }
}
