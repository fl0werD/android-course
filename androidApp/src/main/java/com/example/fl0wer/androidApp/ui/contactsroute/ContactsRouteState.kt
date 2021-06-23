package com.example.fl0wer.androidApp.ui.contactsroute

import com.example.fl0wer.androidApp.data.directions.DirectionParcelable
import com.example.fl0wer.androidApp.data.locations.LocationParcelable
import com.example.fl0wer.androidApp.ui.UiState
import com.example.fl0wer.domain.directions.Direction
import com.google.android.gms.maps.model.LatLng
import kotlinx.parcelize.Parcelize

sealed class ContactsRouteState : UiState {
    @Parcelize
    object Loading : ContactsRouteState()

    @Parcelize
    data class Idle(
        val route: List<LatLng>,
    ) : ContactsRouteState()

    @Parcelize
    object RouteNotFound : ContactsRouteState()
}
