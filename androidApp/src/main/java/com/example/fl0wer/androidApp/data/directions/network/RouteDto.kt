package com.example.fl0wer.androidApp.data.directions.network

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class RouteDto(
    @SerializedName("bounds")
    val bounds: BoundsDto,
    @SerializedName("legs")
    val legs: List<LegDto>,
) : Parcelable

@Parcelize
data class BoundsDto(
    @SerializedName("northeast")
    val northeast: LatLonDto,
    @SerializedName("southwest")
    val southwest: LatLonDto,
) : Parcelable

@Parcelize
data class LegDto(
    @SerializedName("steps")
    val steps: List<StepDto>,
) : Parcelable

@Parcelize
data class StepDto(
    @SerializedName("polyline")
    val polyline: PointsDto,
) : Parcelable

@Parcelize
data class PointsDto(
    @SerializedName("points")
    val points: String,
) : Parcelable

@Parcelize
data class LatLonDto(
    @SerializedName("lat")
    val latitude: Double,
    @SerializedName("lng")
    val longitude: Double,
) : Parcelable
