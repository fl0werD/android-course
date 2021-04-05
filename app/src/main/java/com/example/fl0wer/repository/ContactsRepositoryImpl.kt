package com.example.fl0wer.repository

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import com.example.fl0wer.Const
import com.example.fl0wer.Contact
import com.example.fl0wer.R
import com.example.fl0wer.dispatchers.DispatchersProvider
import com.example.fl0wer.setTimer
import kotlinx.coroutines.withContext
import java.util.*

class ContactsRepositoryImpl(
    private val context: Context,
    private val dispatchersProvider: DispatchersProvider,
) : ContactsRepository {
    private fun handleContact(data: Cursor): Contact? {
        data.use {
            while (data.moveToNext()) {
                val rowId = it.getInt(it.getColumnIndex(ContactsContract.Contacts._ID))
                val lookupKey = it.getString(it.getColumnIndex(ContactsContract.Data.LOOKUP_KEY))
                val name =
                    it.getString(it.getColumnIndex(ContactsContract.Data.DISPLAY_NAME_PRIMARY))
                if (it.getInt(it.getColumnIndex(ContactsContract.Data.HAS_PHONE_NUMBER)) > 0) {
                    context.contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY + " = ?",
                        arrayOf(lookupKey),
                        null,
                    )?.use { phone ->
                        while (phone.moveToNext()) {
                            val phoneNumber =
                                phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                            return Contact(
                                rowId,
                                lookupKey,
                                R.drawable.ic_contact,
                                name,
                                phoneNumber, "",
                                "", "",
                                "",
                                0,
                            )
                        }
                    }
                }
            }
        }
        return null
    }

    private fun handleContactDetails(lookupKey: String, data: Cursor): Contact? {
        data.use {
            while (data.moveToNext()) {
                val rowId = it.getInt(it.getColumnIndex(ContactsContract.Contacts._ID))
                val name =
                    it.getString(it.getColumnIndex(ContactsContract.Data.DISPLAY_NAME_PRIMARY))
                var phonePrimary = ""
                var phoneSecondary = ""
                var emailPrimary = ""
                var emailSecondary = ""
                if (it.getInt(it.getColumnIndex(ContactsContract.Data.HAS_PHONE_NUMBER)) > 0) {
                    context.contentResolver.query(
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

                context.contentResolver.query(
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
                return Contact(
                    rowId,
                    lookupKey,
                    R.drawable.ic_contact,
                    name,
                    phonePrimary, phoneSecondary,
                    emailPrimary, emailSecondary,
                    "",
                    0,
                )
            }
        }
        return null
    }

    override fun birthdayNotice(contact: Contact): Boolean {
        val alarmManager =
            (context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?) ?: return false

        val birthdayNoticeIntent = PendingIntent.getBroadcast(
            context,
            contact.rowId,
            Intent(Const.RECEIVER_INTENT_ACTION_CONTACT_BIRTHDAY),
            PendingIntent.FLAG_NO_CREATE
        )
        return when (birthdayNoticeIntent) {
            null -> {
                val pendingIntent = Intent(Const.RECEIVER_INTENT_ACTION_CONTACT_BIRTHDAY).let {
                    it.putExtra(Const.NOTICE_BIRTHDAY_EXTRA_CONTACT_ID, contact.lookupKey)
                    it.putExtra(
                        Const.NOTICE_BIRTHDAY_EXTRA_TEXT,
                        context.getString(R.string.birthday_notice, contact.name)
                    )
                    PendingIntent.getBroadcast(
                        context, contact.rowId, it,
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )
                }
                val nextBirthday = GregorianCalendar().apply {
                    val currentDate = GregorianCalendar()
                    val currentYear = currentDate.get(Calendar.YEAR)
                    timeInMillis = contact.birthdayTimestamp
                    set(Calendar.YEAR, currentYear)
                    if (timeInMillis < currentDate.timeInMillis) {
                        add(Calendar.YEAR, currentYear + 1)
                    }
                    if (get(Calendar.MONTH) == Calendar.FEBRUARY &&
                        get(Calendar.DAY_OF_MONTH) == 29 &&
                        !isLeapYear(currentYear)
                    ) {
                        add(Calendar.DAY_OF_YEAR, 1)
                    }
                }
                alarmManager.setTimer(
                    AlarmManager.RTC,
                    nextBirthday.timeInMillis,
                    pendingIntent
                )
                true
            }
            else -> {
                alarmManager.cancel(birthdayNoticeIntent)
                birthdayNoticeIntent.cancel()
                false
            }
        }
    }

    override suspend fun getFirstContact() = withContext(dispatchersProvider.default) {
        val projection = arrayOf(
            ContactsContract.Contacts._ID,
            ContactsContract.Data.LOOKUP_KEY,
            ContactsContract.Data.DISPLAY_NAME_PRIMARY,
            ContactsContract.Data.HAS_PHONE_NUMBER,
        )
        val data = context.contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            projection,
            null,
            null,
            null,
        )
        val contacts = mutableListOf<Contact>()
        if (data != null) {
            val contact = handleContact(data)
            if (contact != null) {
                contacts.add(contact)
            }
        }
        contacts.firstOrNull()
    }

    override suspend fun getContactById(lookupKey: String) =
        withContext(dispatchersProvider.default) {
            val contactUri = Uri.withAppendedPath(
                ContactsContract.Contacts.CONTENT_LOOKUP_URI,
                Uri.encode(lookupKey)
            )
            val data = context.contentResolver.query(
                contactUri,
                null,
                null,
                null,
                null,
            )
            if (data == null) {
                return@withContext null
            }
            handleContactDetails(lookupKey, data)
        }
}
