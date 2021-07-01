package com.example.fl0wer.androidApp.ui.contactlocation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ContactLocationScreenParams(
    val contactId: Int,
) : Parcelable
