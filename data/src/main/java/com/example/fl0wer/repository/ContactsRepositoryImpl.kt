package com.example.fl0wer.repository

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import com.example.fl0wer.Contact
import com.example.fl0wer.contacts.ContactsRepository
import com.example.fl0wer.dispatchers.DispatchersProvider
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.GregorianCalendar
import java.util.Locale
import kotlinx.coroutines.withContext

class ContactsRepositoryImpl(
    private val context: Context,
    private val dispatchersProvider: DispatchersProvider,
) : ContactsRepository {
    private val contacts = mutableListOf<Contact>()

    override fun contacts(): List<Contact> = contacts

    override suspend fun loadContacts() =
        withContext(dispatchersProvider.default) {
            context.contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                arrayOf(
                    ContactsContract.Contacts._ID,
                    ContactsContract.Data.LOOKUP_KEY,
                    ContactsContract.Data.DISPLAY_NAME_PRIMARY,
                ),
                null,
                null,
                null,
            )?.use {
                while (it.moveToNext()) {
                    val contact = handleContact(it)
                    if (contact != null) {
                        contacts.add(contact)
                    }
                }
            }
            contacts
        }

    override suspend fun contact(lookupKey: String) =
        withContext(dispatchersProvider.default) {
            if (contacts.isNotEmpty()) {
                return@withContext contacts.firstOrNull {
                    it.lookupKey == lookupKey
                }
            }

            val contactUri = Uri.withAppendedPath(
                ContactsContract.Contacts.CONTENT_LOOKUP_URI,
                Uri.encode(lookupKey)
            )
            context.contentResolver.query(
                contactUri,
                null,
                null,
                null,
                null,
            )?.use {
                while (it.moveToNext()) {
                    return@withContext handleContact(it)
                }
            }
            return@withContext null
        }

    private fun handleContact(data: Cursor): Contact? {
        val id = data.getInt(ContactsContract.Contacts._ID) ?: return null
        val lookupKey = data.getString(ContactsContract.Data.LOOKUP_KEY) ?: return null
        val name = data.getString(ContactsContract.Data.DISPLAY_NAME_PRIMARY) ?: return null
        var phone = ""
        var phone2 = ""
        var email = ""
        var email2 = ""
        var birthdayMonth = -1
        var birthdayDayOfMonth = -1
        var note = ""

        context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(
                ContactsContract.CommonDataKinds.Phone.TYPE,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
            ),
            ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY + " = ?",
            arrayOf(lookupKey),
            null,
        )?.use {
            while (it.moveToNext()) {
                val number = it.getString(ContactsContract.CommonDataKinds.Phone.NUMBER) ?: continue
                when (it.getInt(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE))) {
                    ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE -> phone = number
                    else -> phone2 = number
                }
            }
        }

        context.contentResolver.query(
            ContactsContract.CommonDataKinds.Email.CONTENT_LOOKUP_URI,
            arrayOf(
                ContactsContract.CommonDataKinds.Email.TYPE,
                ContactsContract.CommonDataKinds.Email.LABEL,
            ),
            ContactsContract.CommonDataKinds.Email.LOOKUP_KEY + " = ?",
            arrayOf(lookupKey),
            null,
        )?.use {
            while (it.moveToNext()) {
                val label = it.getString(ContactsContract.CommonDataKinds.Email.LABEL) ?: continue
                when (it.getInt(it.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE))) {
                    ContactsContract.CommonDataKinds.Email.TYPE_WORK -> email = label
                    else -> email2 = label
                }
            }
        }

        context.contentResolver.query(
            ContactsContract.Data.CONTENT_URI,
            arrayOf(ContactsContract.CommonDataKinds.Event.START_DATE),
            ContactsContract.CommonDataKinds.Event.LOOKUP_KEY + "= ? AND " +
                ContactsContract.Data.MIMETYPE + "= ? AND " +
                ContactsContract.CommonDataKinds.Event.TYPE + "=" +
                ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY,
            arrayOf(
                lookupKey,
                ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE,
            ),
            null,
        )?.use {
            while (it.moveToNext()) {
                val startDate =
                    it.getString(ContactsContract.CommonDataKinds.Event.START_DATE) ?: continue
                val formatter = SimpleDateFormat("MM-dd", Locale.getDefault())
                try {
                    val date = formatter.parse(startDate)
                    GregorianCalendar().apply {
                        timeInMillis = date.time
                        birthdayMonth = get(GregorianCalendar.MONTH)
                        birthdayDayOfMonth = get(GregorianCalendar.DAY_OF_MONTH)
                    }
                } catch (e: ParseException) {
                    continue
                }
            }
        }

        context.contentResolver.query(
            ContactsContract.Data.CONTENT_URI,
            arrayOf(ContactsContract.CommonDataKinds.Note.NOTE),
            ContactsContract.Data.LOOKUP_KEY + "= ? AND " +
                ContactsContract.Data.MIMETYPE + "= ?",
            arrayOf(
                lookupKey,
                ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE,
            ),
            null,
        )?.use {
            while (it.moveToNext()) {
                val notes = it.getString(ContactsContract.CommonDataKinds.Note.NOTE) ?: continue
                note = notes
            }
        }

        return Contact(
            id,
            lookupKey,
            0,
            name,
            phone, phone2,
            email, email2,
            birthdayMonth, birthdayDayOfMonth,
            note,
        )
    }

    private fun Cursor.getInt(column: String): Int? =
        when (val index = getColumnIndex(column)) {
            -1 -> null
            else -> getInt(index)
        }

    private fun Cursor.getString(column: String): String? =
        when (val index = getColumnIndex(column)) {
            -1 -> null
            else -> getString(index)
        }
}
