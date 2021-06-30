package com.example.fl0wer.androidApp.ui.contactlocation

import com.example.fl0wer.androidApp.data.locations.LocationMapper.toParcelable
import com.example.fl0wer.androidApp.domain.locations.LocationInteractor
import com.example.fl0wer.androidApp.ui.core.BaseViewModel
import com.example.fl0wer.androidApp.ui.core.dispatchers.DispatchersProvider
import com.example.fl0wer.androidApp.ui.nullOr
import com.github.terrakok.modo.Modo
import com.google.android.gms.maps.model.LatLng
import javax.inject.Inject
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ContactLocationViewModel @Inject constructor(
    modo: Modo,
    private val locationInteractor: LocationInteractor,
    private val dispatchersProvider: DispatchersProvider,
    private val screenParams: ContactLocationScreenParams,
) : BaseViewModel<ContactLocationState>(
    initState = ContactLocationState.Loading,
    modo,
) {
    init {
        subscribeLocation(screenParams.contactId)
    }

    fun mapLongClicked(location: LatLng) {
        uiState.nullOr<ContactLocationState.Idle>() ?: return
        vmScope.launch {
            locationInteractor.mapClicked(
                screenParams.contactId,
                location.latitude,
                location.longitude,
            )
        }
    }

    private fun subscribeLocation(contact: Int) = vmScope.launch {
        locationInteractor.observeLocation(contact)
            .map {
                val currentState = uiState
                if (currentState is ContactLocationState.Idle) {
                    currentState.copy(
                        firstEntryZoom = false,
                        location = it?.toParcelable()
                    )
                } else {
                    ContactLocationState.Idle(
                        firstEntryZoom = true,
                        location = it?.toParcelable()
                    )
                }
            }
            .flowOn(dispatchersProvider.io)
            .collect { newState ->
                updateState { newState }
            }
    }
}
