package com.example.fl0wer.androidApp.ui.contactlocations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.fl0wer.androidApp.data.locations.LocationMapper.toParcelable
import com.example.fl0wer.androidApp.ui.contactlist.ContactListState
import com.example.fl0wer.androidApp.ui.contactlist.ContactListViewModelFactory
import com.example.fl0wer.domain.contacts.ContactsInteractor
import com.example.fl0wer.domain.locations.LocationInteractor
import com.github.terrakok.modo.Modo
import com.github.terrakok.modo.back
import dagger.assisted.AssistedInject
import java.io.IOException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import timber.log.Timber

class ContactLocationsViewModel @AssistedInject constructor(
    private val locationInteractor: LocationInteractor,
    private val modo: Modo,
) : ViewModel() {
    private val _uiState = MutableStateFlow<ContactLocationsState>(ContactLocationsState.Loading)
    val uiState: StateFlow<ContactLocationsState> get() = _uiState
    private val vmScope = viewModelScope + CoroutineExceptionHandler { _, e ->
        if (e !is CancellationException) {
            Timber.e(e)
        }
    }

    init {
        loadLocations()
    }

    fun backPressed() {
        modo.back()
    }

    private fun loadLocations() {
        vmScope.launch {
            try {
                val locations = locationInteractor.locations()
                _uiState.value = ContactLocationsState.Idle(locations.toParcelable())
            } catch (e: IOException) {
                //_uiState.value = ContactLocationsState.Failure
            }
        }
    }

    companion object {
        fun provideFactory(
            assistedFactory: ContactLocationsViewModelFactory,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create() as T
            }
        }
    }
}
