package com.example.fl0wer.androidApp.data.directions

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DirectionParcelable(
    val boolean: Boolean,
) : Parcelable
