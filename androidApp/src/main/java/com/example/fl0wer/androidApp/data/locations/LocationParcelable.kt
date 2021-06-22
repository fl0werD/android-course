package com.example.fl0wer.androidApp.data.locations

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LocationParcelable(
    val id: Int,
    val latitude: Double,
    val longitude: Double,
) : Parcelable
