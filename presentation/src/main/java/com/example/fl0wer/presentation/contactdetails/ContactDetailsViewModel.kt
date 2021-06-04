package com.example.fl0wer.presentation.contactdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.fl0wer.contacts.ContactsInteractor
import com.example.fl0wer.contacts.ReminderInteractor
import com.example.fl0wer.mapper.ContactMapper
import com.example.fl0wer.mapper.ContactParcelableMapper
import com.example.fl0wer.nullOr
import com.github.terrakok.modo.Modo
import com.github.terrakok.modo.back
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import timber.log.Timber

@Suppress("SwallowedException")
class ContactDetailsViewModel @AssistedInject constructor(
    private val contactsInteractor: ContactsInteractor,
    private val reminderInteractor: ReminderInteractor,
    private val modo: Modo,
    @Assisted contactId: String,
) : ViewModel() {
    private val _uiState = MutableStateFlow<ContactDetailsState>(ContactDetailsState.Loading)
    val uiState: StateFlow<ContactDetailsState> get() = _uiState
    private val vmScope = viewModelScope + CoroutineExceptionHandler { _, e ->
        if (e !is CancellationException) {
            Timber.e(e)
        }
    }

    init {
        getContactById(contactId)
    }

    fun changeBirthdayNotice() {
        val currentState = uiState.value.nullOr<ContactDetailsState.Idle>() ?: return
        vmScope.launch {
            try {
                reminderInteractor.changeBirthdayReminder(ContactParcelableMapper.map(currentState.contact))
                _uiState.value = currentState.copy(birthdayReminder = !currentState.birthdayReminder)
            } catch (e: IOException) {
                _uiState.value = ContactDetailsState.Failure
            }
        }
    }

    fun backPressed() {
        modo.back()
    }

    private fun getContactById(lookupKey: String) {
        vmScope.launch {
            _uiState.value = ContactDetailsState.Loading
            try {
                val contact = contactsInteractor.contact(lookupKey)
                if (contact != null) {
                    _uiState.value = ContactDetailsState.Idle(
                        ContactMapper.map(contact),
                        reminderInteractor.birthdayReminder(contact),
                    )
                } else {
                    _uiState.value = ContactDetailsState.Failure
                }
            } catch (e: IOException) {
                _uiState.value = ContactDetailsState.Failure
            }
        }
    }

    companion object {
        fun provideFactory(
            assistedFactory: ContactDetailsViewModelFactory,
            contactId: String,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(contactId) as T
            }
        }
    }
}
