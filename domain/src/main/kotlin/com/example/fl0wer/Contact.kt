package com.example.fl0wer

data class Contact(
    val id: Int,
    val lookupKey: String,
    val photo: Int,
    val name: String,
    val phone: String,
    val phone2: String,
    val email: String,
    val email2: String,
    val birthdayMonth: Int,
    val birthdayDayOfMonth: Int,
    val note: String,
)
