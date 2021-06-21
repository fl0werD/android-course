package com.example.fl0wer.androidApp.data.contacts.network

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class ContactDto(
    @SerializedName("id")
    val id: Int,
) : Parcelable
