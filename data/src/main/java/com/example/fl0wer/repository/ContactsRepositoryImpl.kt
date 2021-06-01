package com.example.fl0wer.repository

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import android.util.Log
import com.example.fl0wer.Const
import com.example.fl0wer.Contact
import com.example.fl0wer.contacts.ContactsRepository
import com.example.fl0wer.dispatchers.DispatchersProvider
import com.example.fl0wer.getAlarmManager
import com.example.fl0wer.setTimer
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlinx.coroutines.withContext

class ContactsRepositoryImpl @Inject constructor(
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

    override suspend fun contact(lookupKey: String): Contact? =
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

    override suspend fun getBirthdayReminder(contact: Contact): Boolean =
        withContext(dispatchersProvider.default) {
            val birthdayNoticeIntent = PendingIntent.getBroadcast(
                context,
                contact.id,
                Intent(Const.RECEIVER_INTENT_ACTION_CONTACT_BIRTHDAY),
                PendingIntent.FLAG_NO_CREATE,
            )
            birthdayNoticeIntent != null
        }

    override suspend fun addBirthdayReminder(contact: Contact, nextBirthday: Long) =
        withContext(dispatchersProvider.default) {
            val alarmManager = getAlarmManager(context) ?: return@withContext
            val pendingIntent = Intent(Const.RECEIVER_INTENT_ACTION_CONTACT_BIRTHDAY).let {
                it.putExtra(Const.NOTICE_BIRTHDAY_EXTRA_CONTACT_ID, contact.lookupKey)
                PendingIntent.getBroadcast(
                    context,
                    contact.id,
                    it,
                    PendingIntent.FLAG_UPDATE_CURRENT,
                )
            }
            alarmManager.setTimer(AlarmManager.RTC, nextBirthday, pendingIntent)
        }

    override suspend fun removeBirthdayReminder(contact: Contact) =
        withContext(dispatchersProvider.default) {
            val alarmManager = getAlarmManager(context) ?: return@withContext
            val birthdayNoticeIntent = PendingIntent.getBroadcast(
                context,
                contact.id,
                Intent(Const.RECEIVER_INTENT_ACTION_CONTACT_BIRTHDAY),
                PendingIntent.FLAG_NO_CREATE,
            )
            alarmManager.cancel(birthdayNoticeIntent)
            birthdayNoticeIntent.cancel()
        }

    private fun handleContact(data: Cursor): Contact? {
        val id = data.getInt(ContactsContract.Contacts._ID) ?: return null
        val lookupKey = data.getString(ContactsContract.Data.LOOKUP_KEY) ?: return null
        val name = data.getString(ContactsContract.Data.DISPLAY_NAME_PRIMARY) ?: return null
        var phone = ""
        var phone2 = ""
        var email = ""
        var email2 = ""
        var birthdayTimestamp = -1L
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
                val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                try {
                    val date = formatter.parse(startDate)
                    birthdayTimestamp = date.time
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
            birthdayTimestamp,
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
