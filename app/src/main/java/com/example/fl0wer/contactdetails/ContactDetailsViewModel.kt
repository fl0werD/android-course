package com.example.fl0wer.contactdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.fl0wer.nullOr
import com.example.fl0wer.repository.ContactsRepository
import com.github.terrakok.modo.Modo
import com.github.terrakok.modo.back
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import java.io.IOException
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import timber.log.Timber

class ContactDetailsViewModel @AssistedInject constructor(
    private val contactsRepository: ContactsRepository,
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
        val birthdayNotice = contactsRepository.birthdayNotice(currentState.contact)
        _uiState.value = currentState.copy(
            birthdayNotice = birthdayNotice,
        )
    }

    fun backPressed() {
        modo.back()
    }

    private fun getContactById(lookupKey: String) {
        vmScope.launch {
            _uiState.value = ContactDetailsState.Loading
            try {
                val contact = contactsRepository.getContactById(lookupKey)
                if (contact != null) {
                    _uiState.value = ContactDetailsState.Idle(
                        contact,
                        false
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
