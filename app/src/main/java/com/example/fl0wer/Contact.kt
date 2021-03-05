package com.example.fl0wer

import androidx.annotation.DrawableRes

data class Contact(
    val id: Int,
    @DrawableRes
    val photo: Int,
    val name: String,
    val phone: String,
    val phone2: String,
    val email: String,
    val email2: String,
    val notes: String,
)
