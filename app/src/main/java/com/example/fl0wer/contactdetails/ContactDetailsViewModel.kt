package com.example.fl0wer.contactdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fl0wer.CiceroneHolder
import com.example.fl0wer.repository.ContactsRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import timber.log.Timber
import java.io.IOException

class ContactDetailsViewModel(
    private val contactsRepository: ContactsRepository,
    param: String,
) : ViewModel() {
    private val screenState = MutableLiveData<ContactDetailsState>()
    private val exceptionHandler = CoroutineExceptionHandler { _, t ->
        Timber.e(t)
    }
    private val vmScope = viewModelScope + exceptionHandler

    init {
        getContactById(param)
    }

    fun getScreenState(): LiveData<ContactDetailsState> {
        return screenState
    }

    fun changeBirthdayNotice() {
        val currentState = when (screenState.value) {
            is ContactDetailsState.Idle -> {
                screenState.value as ContactDetailsState.Idle
            }
            else -> null
        } ?: return

        val birthdayNotice = contactsRepository.birthdayNotice(currentState.contact)
        screenState.value = currentState.copy(
            birthdayNotice = birthdayNotice,
        )
    }

    fun backPressed() {
        CiceroneHolder.router.exit()
    }

    private fun getContactById(lookupKey: String) {
        vmScope.launch {
            screenState.value = ContactDetailsState.Loading
            try {
                val contact = contactsRepository.getContactById(lookupKey)
                if (contact != null) {
                    screenState.value = ContactDetailsState.Idle(
                        contact,
                        false,
                    )
                } else {
                    screenState.value = ContactDetailsState.Error
                }
            } catch (e: IOException) {
                screenState.value = ContactDetailsState.Error
            }
        }
    }
}
