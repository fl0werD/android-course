package com.example.fl0wer.androidApp.data.contacts

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import com.example.fl0wer.androidApp.domain.contacts.Contact
import com.example.fl0wer.androidApp.domain.contacts.ContactsRepository
import com.example.fl0wer.androidApp.domain.core.dispatchers.DispatchersProvider
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
            contacts.clear()
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

    @Suppress("ReturnCount")
    private fun handleContact(data: Cursor): Contact? {
        val id = data.getInt(ContactsContract.Contacts._ID) ?: return null
        val lookupKey = data.getString(ContactsContract.Data.LOOKUP_KEY) ?: return null
        val name = data.getString(ContactsContract.Data.DISPLAY_NAME_PRIMARY) ?: return null
        val phones = getPhones(lookupKey)
        val emails = getEmails(lookupKey)
        val birthday = getBirthday(lookupKey)
        val note = getNote(lookupKey)

        return Contact(
            id,
            lookupKey,
            0,
            name,
            phones[0], phones[1],
            emails[0], emails[1],
            birthday[0], birthday[1],
            note,
        )
    }

    private fun getPhones(lookupKey: String): MutableList<String> {
        val phones = mutableListOf("", "")
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
                when (it.getInt(ContactsContract.CommonDataKinds.Phone.TYPE)) {
                    ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE -> phones[0] = number
                    else -> phones[1] = number
                }
            }
        }
        return phones
    }

    private fun getEmails(lookupKey: String): MutableList<String> {
        val emails = mutableListOf("", "")
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
                when (it.getInt(ContactsContract.CommonDataKinds.Email.TYPE)) {
                    ContactsContract.CommonDataKinds.Email.TYPE_WORK -> emails[0] = label
                    else -> emails[1] = label
                }
            }
        }
        return emails
    }

    @Suppress("NestedBlockDepth")
    private fun getBirthday(lookupKey: String): MutableList<Int> {
        val birthday = mutableListOf(-1, -1)
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
                    formatter.parse(startDate)?.let {
                        GregorianCalendar().apply {
                            timeInMillis = it.time
                            birthday[0] = get(GregorianCalendar.MONTH)
                            birthday[1] = get(GregorianCalendar.DAY_OF_MONTH)
                        }
                    }
                } catch (e: ParseException) {
                    return birthday
                }
            }
        }
        return birthday
    }

    private fun getNote(lookupKey: String): String {
        var note = ""
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
        return note
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
