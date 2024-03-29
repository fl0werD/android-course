package com.example.fl0wer.androidApp.ui

import android.os.Parcelable

interface UiState : Parcelable

inline fun <reified S : UiState> UiState.nullOr(): S? =
    if (this is S) {
        this
    } else {
        null
    }
