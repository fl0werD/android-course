package com.example.fl0wer.androidApp.data.contacts

import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class ContactParcelable(
    val id: Int,
    val lookupKey: String,
    @DrawableRes
    val photo: Int,
    val name: String,
    val phone: String,
    val phone2: String,
    val email: String,
    val email2: String,
    val birthdayMonth: Int,
    val birthdayDayOfMonth: Int,
    val note: String,
) : Parcelable
