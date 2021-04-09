package com.example.fl0wer.contactdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fl0wer.CiceroneHolder
import com.example.fl0wer.contactlist.ContactListState
import com.example.fl0wer.nullOr
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
    private val stateLiveData = MutableLiveData<ContactDetailsState>()
    private val state: ContactDetailsState
        get() = stateLiveData.value ?: throw IllegalStateException("state value should not be null")
    private val exceptionHandler = CoroutineExceptionHandler { _, t ->
        Timber.e(t)
    }
    private val vmScope = viewModelScope + exceptionHandler

    init {
        getContactById(param)
    }

    fun getScreenState(): LiveData<ContactDetailsState> {
        return stateLiveData
    }

    fun changeBirthdayNotice() {
        val currentState = state.nullOr<ContactDetailsState.Idle>() ?: return
        val birthdayNotice = contactsRepository.birthdayNotice(currentState.contact)
        stateLiveData.value = currentState.copy(
            birthdayNotice = birthdayNotice,
        )
    }

    fun backPressed() {
        CiceroneHolder.router.exit()
    }

    private fun getContactById(lookupKey: String) {
        vmScope.launch {
            stateLiveData.value = ContactDetailsState.Loading
            try {
                val contact = contactsRepository.getContactById(lookupKey)
                if (contact != null) {
                    stateLiveData.value = ContactDetailsState.Idle(
                        contact,
                        false,
                    )
                } else {
                    stateLiveData.value = ContactDetailsState.Error
                }
            } catch (e: IOException) {
                stateLiveData.value = ContactDetailsState.Error
            }
        }
    }
}
