package com.example.fl0wer.androidApp.ui.contactsroute

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ContactsRouteScreenParams(
    val startContactId: Int,
) : Parcelable
