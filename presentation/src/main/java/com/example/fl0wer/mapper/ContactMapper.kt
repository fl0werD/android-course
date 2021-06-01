package com.example.fl0wer.mapper

import com.example.fl0wer.Contact
import com.example.fl0wer.data.ContactParcelable

object ContactMapper : Mapper<Contact, ContactParcelable> {
    override fun map(from: Contact) = ContactParcelable(
        id = from.id,
        lookupKey = from.lookupKey,
        photo = from.photo,
        name = from.name,
        phone = from.phone,
        phone2 = from.phone2,
        email = from.email,
        email2 = from.email2,
        birthdayTimestamp = from.birthdayTimestamp,
        note = from.note,
    )
}

object ContactParcelableMapper : Mapper<ContactParcelable, Contact> {
    override fun map(from: ContactParcelable) = Contact(
        id = from.id,
        lookupKey = from.lookupKey,
        photo = from.photo,
        name = from.name,
        phone = from.phone,
        phone2 = from.phone2,
        email = from.email,
        email2 = from.email2,
        birthdayTimestamp = from.birthdayTimestamp,
        note = from.note,
    )
}
