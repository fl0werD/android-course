package com.example.fl0wer.androidApp.ui.contactsroute

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.fl0wer.androidApp.data.directions.toParcelable
import com.example.fl0wer.domain.directions.DirectionInteractor
import com.example.fl0wer.domain.locations.LocationInteractor
import com.github.terrakok.modo.Modo
import com.github.terrakok.modo.back
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import timber.log.Timber
import java.io.IOException

class ContactsRouteViewModel @AssistedInject constructor(
    private val locationInteractor: LocationInteractor,
    private val directionInteractor: DirectionInteractor,
    private val modo: Modo,
    @Assisted("startContact") private val startContactId: Int,
    @Assisted("endContact") private val endContactId: Int,
) : ViewModel() {
    private val _uiState = MutableStateFlow<ContactsRouteState>(ContactsRouteState.Loading)
    val uiState: StateFlow<ContactsRouteState> get() = _uiState
    private val vmScope = viewModelScope + CoroutineExceptionHandler { _, e ->
        if (e !is CancellationException) {
            Timber.e(e)
        }
    }

    init {
        buildRoute()
    }

    fun backPressed() {
        modo.back()
    }

    private fun buildRoute() {
        vmScope.launch {
            _uiState.value = ContactsRouteState.Loading
            try {
                /*val start = locationInteractor.location(startContactId)
                val end = locationInteractor.location(endContactId)
                if (start != null && end != null) {
                    val route = directionInteractor.route(start.address, end.address)
                    if (route != null) {
                        _uiState.value = ContactsRouteState.Idle(route.toParcelable())
                    } else {
                        _uiState.value = ContactsRouteState.RouteNotFound
                    }
                } else {
                    _uiState.value = ContactsRouteState.EmptyAddress
                }*/
            } catch (e: IOException) {
                Timber.e(e)
            }
        }
    }

    companion object {
        fun provideFactory(
            assistedFactory: ContactsRouteViewModelFactory,
            startContactId: Int,
            endContactId: Int,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(startContactId, endContactId) as T
            }
        }
    }
}
