package com.example.fl0wer.androidApp.data.directions

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RouteParcelable(
    val bounds: BoundsParcelable,
    val points: List<LatLonParcelable>,
) : Parcelable

@Parcelize
data class BoundsParcelable(
    val northeast: LatLonParcelable,
    val southwest: LatLonParcelable,
) : Parcelable

@Parcelize
data class LatLonParcelable(
    val latitude: Double,
    val longitude: Double,
) : Parcelable
