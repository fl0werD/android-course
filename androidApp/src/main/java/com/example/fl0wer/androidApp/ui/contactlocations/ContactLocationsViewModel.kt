package com.example.fl0wer.androidApp.ui.contactlocations

import com.example.fl0wer.androidApp.data.locations.LocationMapper.toParcelable
import com.example.fl0wer.androidApp.domain.locations.LocationInteractor
import com.example.fl0wer.androidApp.ui.core.BaseViewModel
import com.github.terrakok.modo.Modo
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.launch
import timber.log.Timber

class ContactLocationsViewModel @Inject constructor(
    modo: Modo,
    private val locationInteractor: LocationInteractor,
) : BaseViewModel<ContactLocationsState>(
    initState = ContactLocationsState.Loading,
    modo,
) {
    init {
        loadLocations()
    }

    private fun loadLocations() {
        vmScope.launch {
            try {
                val locations = locationInteractor.locations()
                updateState { ContactLocationsState.Idle(locations.toParcelable()) }
            } catch (e: IOException) {
                Timber.e(e)
            }
        }
    }
}
