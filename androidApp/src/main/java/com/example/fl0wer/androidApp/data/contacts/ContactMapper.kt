package com.example.fl0wer.androidApp.data.contacts

import com.example.fl0wer.androidApp.domain.contacts.Contact

object ContactMapper {
    fun Contact.toParcelable() = ContactParcelable(
        id = id,
        lookupKey = lookupKey,
        photo = photo,
        name = name,
        phone = phone,
        phone2 = phone2,
        email = email,
        email2 = email2,
        birthdayMonth = birthdayMonth,
        birthdayDayOfMonth = birthdayDayOfMonth,
        note = note,
    )

    fun ContactParcelable.toContact() = Contact(
        id = id,
        lookupKey = lookupKey,
        photo = photo,
        name = name,
        phone = phone,
        phone2 = phone2,
        email = email,
        email2 = email2,
        birthdayMonth = birthdayMonth,
        birthdayDayOfMonth = birthdayDayOfMonth,
        note = note,
    )

    fun List<ContactParcelable>.toContact(): List<Contact> = map { it.toContact() }
}
