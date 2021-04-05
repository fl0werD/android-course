package com.example.fl0wer.contactdetails

import com.example.fl0wer.Contact

sealed class ContactDetailsState {
    data class Idle(
        val contact: Contact,
        val birthdayNotice: Boolean,
    ) : ContactDetailsState()

    object Loading : ContactDetailsState()
    object Error : ContactDetailsState()
}
