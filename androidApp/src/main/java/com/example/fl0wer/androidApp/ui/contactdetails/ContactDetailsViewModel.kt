package com.example.fl0wer.androidApp.ui.contactdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.fl0wer.androidApp.data.contacts.ContactMapper.toContact
import com.example.fl0wer.androidApp.data.contacts.ContactMapper.toParcelable
import com.example.fl0wer.androidApp.data.locations.LocationMapper.toParcelable
import com.example.fl0wer.androidApp.ui.nullOr
import com.example.fl0wer.domain.contacts.ContactsInteractor
import com.example.fl0wer.domain.contacts.ReminderInteractor
import com.example.fl0wer.domain.core.dispatchers.DispatchersProvider
import com.example.fl0wer.domain.locations.LocationInteractor
import com.github.terrakok.modo.Modo
import com.github.terrakok.modo.back
import com.google.android.gms.maps.model.LatLng
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import timber.log.Timber
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

@Suppress("SwallowedException")
class ContactDetailsViewModel @AssistedInject constructor(
    private val contactsInteractor: ContactsInteractor,
    private val reminderInteractor: ReminderInteractor,
    private val locationInteractor: LocationInteractor,
    private val dispatchersProvider: DispatchersProvider,
    private val modo: Modo,
    @Assisted contactLookupKey: String,
) : ViewModel() {
    private val _uiState = MutableStateFlow<ContactDetailsState>(ContactDetailsState.Loading)
    val uiState: StateFlow<ContactDetailsState> get() = _uiState
    private val vmScope = viewModelScope + CoroutineExceptionHandler { _, e ->
        if (e !is CancellationException) {
            Timber.e(e)
        }
    }
    private val contactId = MutableStateFlow(0)

    init {
        loadContact(contactLookupKey)
        subscribeLocation()
    }

    fun changeBirthdayNotice() {
        val currentState = uiState.value.nullOr<ContactDetailsState.Idle>() ?: return
        vmScope.launch {
            try {
                reminderInteractor.changeBirthdayReminder(currentState.contact.toContact())
                _uiState.value =
                    currentState.copy(birthdayReminder = !currentState.birthdayReminder)
            } catch (e: IOException) {
                _uiState.value = ContactDetailsState.Failure
            }
        }
    }

    fun mapClicked(location: LatLng) {
        val currentState = uiState.value.nullOr<ContactDetailsState.Idle>() ?: return
        vmScope.launch {
            locationInteractor.mapClicked(
                currentState.contact.id,
                location.latitude,
                location.longitude,
            )
        }
    }

    fun backPressed() {
        modo.back()
    }

    private fun loadContact(lookupKey: String) {
        vmScope.launch {
            _uiState.value = ContactDetailsState.Loading
            try {
                val contact = contactsInteractor.contact(lookupKey)
                if (contact != null) {
                    _uiState.value = ContactDetailsState.Idle(
                        contact.toParcelable(),
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

    private fun subscribeLocation() = vmScope.launch {
        locationInteractor.observeLocation(1)
            .map {
                val currentState = uiState.value
                if (currentState is ContactDetailsState.Idle) {
                    currentState.copy(
                       location = it?.toParcelable()
                   )
                } else {
                    currentState
                }
            }
            .flowOn(dispatchersProvider.io)
            .collect { newState ->
                _uiState.value = newState
            }
    }

    companion object {
        fun provideFactory(
            assistedFactory: ContactDetailsViewModelFactory,
            contactLookupKey: String,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(contactLookupKey) as T
            }
        }
    }
}
