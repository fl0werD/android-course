package com.example.fl0wer.androidApp.ui.contactlocations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fl0wer.androidApp.data.locations.LocationMapper.toParcelable
import com.example.fl0wer.domain.locations.LocationInteractor
import com.github.terrakok.modo.Modo
import com.github.terrakok.modo.back
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class ContactLocationsViewModel @Inject constructor(
    private val locationInteractor: LocationInteractor,
    private val modo: Modo,
) : ViewModel() {
    private val _uiState = MutableStateFlow<ContactLocationsState>(ContactLocationsState.Loading)
    val uiState get() = _uiState.asStateFlow()
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
                Timber.e(e)
            }
        }
    }
}
