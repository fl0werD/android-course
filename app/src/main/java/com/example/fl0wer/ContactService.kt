package com.example.fl0wer

import android.app.Service
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import android.provider.ContactsContract
import java.lang.ref.WeakReference
import java.util.concurrent.Executors

class ContactService : Service() {
    private val binder: IBinder = ContactBinder()
    private val threadExecutor = Executors.newSingleThreadExecutor()

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    override fun onDestroy() {
        threadExecutor.shutdown()
        super.onDestroy()
    }

    private fun handleContact(data: Cursor) {
        data.use {
            while (data.moveToNext()) {
                val rowId = it.getInt(it.getColumnIndex(ContactsContract.Contacts._ID))
                val lookupKey = it.getString(it.getColumnIndex(ContactsContract.Data.LOOKUP_KEY))
                val name = it.getString(it.getColumnIndex(ContactsContract.Data.DISPLAY_NAME_PRIMARY))
                if (it.getInt(it.getColumnIndex(ContactsContract.Data.HAS_PHONE_NUMBER)) > 0) {
                    contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY + " = ?",
                        arrayOf(lookupKey),
                        null,
                    )?.use { phone ->
                        while (phone.moveToNext()) {
                            val phoneNumber =
                                phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                            val contact = Contact(
                                rowId,
                                lookupKey,
                                R.drawable.ic_contact,
                                name,
                                phoneNumber,
                                "",
                                "",
                                "",
                                "",
                                0,
                            )
                            Contacts.addContact(contact)
                        }
                    }
                }
            }
        }
    }

    private fun handleContactDetails(lookupKey: String, data: Cursor) {
        data.use {
            while (data.moveToNext()) {
                val rowId = it.getInt(it.getColumnIndex(ContactsContract.Contacts._ID))
                val name = it.getString(it.getColumnIndex(ContactsContract.Data.DISPLAY_NAME_PRIMARY))
                var phonePrimary = ""
                var phoneSecondary = ""
                var emailPrimary = ""
                var emailSecondary = ""
                if (it.getInt(it.getColumnIndex(ContactsContract.Data.HAS_PHONE_NUMBER)) > 0) {
                    contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY + " = ?",
                        arrayOf(lookupKey),
                        null,
                    )?.use { phone ->
                        while (phone.moveToNext()) {
                            when (phone.getInt(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE))) {
                                ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE -> {
                                    phonePrimary = phone.getString(
                                        phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                                    )
                                }
                                else -> {
                                    phoneSecondary = phone.getString(
                                        phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                                    )
                                }
                            }
                        }
                    }
                }

                contentResolver.query(
                    ContactsContract.CommonDataKinds.Email.CONTENT_LOOKUP_URI,
                    null,
                    ContactsContract.CommonDataKinds.Email.LOOKUP_KEY + " = ?",
                    arrayOf(lookupKey),
                    null,
                )?.use { email ->
                    while (email.moveToNext()) {
                        when (email.getInt(email.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE))) {
                            ContactsContract.CommonDataKinds.Email.TYPE_WORK -> {
                                emailPrimary =
                                    email.getString(
                                        email.getColumnIndex(
                                            ContactsContract.CommonDataKinds.Email.LABEL
                                        )
                                    )
                            }
                            else -> {
                                emailSecondary =
                                    email.getString(
                                        email.getColumnIndex(
                                            ContactsContract.CommonDataKinds.Email.LABEL
                                        )
                                    )
                            }
                        }
                    }
                }
                val contact = Contact(
                    rowId,
                    lookupKey,
                    R.drawable.ic_contact,
                    name,
                    phonePrimary, phoneSecondary,
                    emailPrimary, emailSecondary,
                    "",
                    0,
                )
                Contacts.updateContactDetails(lookupKey, contact)
            }
        }
    }

    inner class ContactBinder : Binder() {
        val contactService = object : IContactService {
            override fun getFirstContact(resultHandler: (Contact?) -> Unit) {
                val weakReferenceHandler = WeakReference(resultHandler)
                threadExecutor.execute {
                    val projection = arrayOf(
                        ContactsContract.Contacts._ID,
                        ContactsContract.Data.LOOKUP_KEY,
                        ContactsContract.Data.DISPLAY_NAME_PRIMARY,
                        ContactsContract.Data.HAS_PHONE_NUMBER,
                    )
                    val data = contentResolver.query(
                        ContactsContract.Contacts.CONTENT_URI,
                        projection,
                        null,
                        null,
                        null,
                    )
                    Contacts.clearContacts()
                    if (data != null) {
                        handleContact(data)
                    }
                    weakReferenceHandler.get()?.invoke(Contacts.contacts.firstOrNull())
                }
            }

            override fun getContactById(lookupKey: String, resultHandler: (Contact?) -> Unit) {
                val weakReferenceHandler = WeakReference(resultHandler)
                threadExecutor.execute {
                    val contactUri = Uri.withAppendedPath(
                        ContactsContract.Contacts.CONTENT_LOOKUP_URI,
                        Uri.encode(lookupKey)
                    )
                    val data = contentResolver.query(
                        contactUri,
                        null,
                        null,
                        null,
                        null,
                    )
                    if (data != null) {
                        handleContactDetails(lookupKey, data)
                    }
                    weakReferenceHandler.get()?.invoke(Contacts.getUserById(lookupKey))
                }
            }
        }
    }
}
