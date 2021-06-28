@file:Suppress("WildcardImport")

package com.example.fl0wer.androidApp.ui.contactlocation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fl0wer.androidApp.data.locations.LocationMapper.toParcelable
import com.example.fl0wer.androidApp.ui.nullOr
import com.example.fl0wer.domain.core.dispatchers.DispatchersProvider
import com.example.fl0wer.domain.locations.LocationInteractor
import com.github.terrakok.modo.Modo
import com.github.terrakok.modo.back
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import timber.log.Timber
import javax.inject.Inject

class ContactLocationViewModel @Inject constructor(
    private val locationInteractor: LocationInteractor,
    private val dispatchersProvider: DispatchersProvider,
    private val modo: Modo,
    private val screenParams: ContactLocationScreenParams,
) : ViewModel() {
    private val _uiState = MutableStateFlow<ContactLocationState>(ContactLocationState.Loading)
    val uiState get() = _uiState.asStateFlow()
    private val vmScope = viewModelScope + CoroutineExceptionHandler { _, e ->
        if (e !is CancellationException) {
            Timber.e(e)
        }
    }

    init {
        subscribeLocation(screenParams.contactId)
    }

    fun mapLongClicked(location: LatLng) {
        uiState.value.nullOr<ContactLocationState.Idle>() ?: return
        vmScope.launch {
            locationInteractor.mapClicked(
                screenParams.contactId,
                location.latitude,
                location.longitude,
            )
        }
    }

    fun backPressed() {
        modo.back()
    }

    private fun subscribeLocation(contact: Int) = vmScope.launch {
        locationInteractor.observeLocation(contact)
            .map {
                ContactLocationState.Idle(
                    location = it?.toParcelable()
                )
            }
            .flowOn(dispatchersProvider.io)
            .collect { newState ->
                _uiState.value = newState
            }
    }
}
