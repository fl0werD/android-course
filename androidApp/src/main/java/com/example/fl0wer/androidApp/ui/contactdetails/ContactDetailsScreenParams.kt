package com.example.fl0wer.androidApp.ui.contactdetails

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ContactDetailsScreenParams(
    val contactLookupKey: String,
) : Parcelable
