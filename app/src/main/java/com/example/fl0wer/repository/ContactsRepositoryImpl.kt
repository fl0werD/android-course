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
import com.example.fl0wer.setTimer
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import java.io.IOException
import java.util.Calendar
import java.util.GregorianCalendar
import java.util.Optional

class ContactsRepositoryImpl(
    private val context: Context,
) : ContactsRepository {
    private val contacts = mutableListOf<Contact>()

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

    override fun getContacts(): Observable<List<Contact>> =
        Observable.defer {
            val projection = arrayOf(
                ContactsContract.Contacts._ID,
                ContactsContract.Data.LOOKUP_KEY,
                ContactsContract.Data.DISPLAY_NAME_PRIMARY,
            )
            contacts.clear()
            context.contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                projection,
                null,
                null,
                null,
            )?.use {
                while (it.moveToNext()) {
                    val contact = handleContact(it)
                    contacts.add(contact)
                }
            }
            return@defer Observable.just(contacts)
        }

    override fun getContactById(lookupKey: String): Single<Contact> =
        Single.defer {
            if (contacts.isNotEmpty()) {
                return@defer Single.just(contacts.firstOrNull {
                    it.lookupKey == lookupKey
                })
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
                    return@defer Single.just(handleContact(it))
                }
            }
            return@defer Single.error(IOException("Invalid contact"))
        }

    override fun getSearchedContacts(nameFilter: String): Observable<List<Contact>> =
        Observable.defer {
            if (nameFilter.isNotEmpty()) {
                return@defer Observable.just(contacts.filter {
                    it.name.contains(nameFilter, true)
                })
            } else {
                return@defer Observable.just(contacts)
            }
        }

    private fun handleContact(data: Cursor): Contact {
        val rowId = data.getInt(data.getColumnIndex(ContactsContract.Contacts._ID))
        val lookupKey = data.getString(ContactsContract.Data.LOOKUP_KEY)
        val name = data.getString(ContactsContract.Data.DISPLAY_NAME_PRIMARY)
        var phone = ""
        var phone2 = ""
        var email = ""
        var email2 = ""

        context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY + " = ?",
            arrayOf(lookupKey),
            null,
        )?.use {
            while (it.moveToNext()) {
                when (it.getInt(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE))) {
                    ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE -> {
                        phone = it.getString(ContactsContract.CommonDataKinds.Phone.NUMBER)
                    }
                    else -> {
                        phone2 = it.getString(ContactsContract.CommonDataKinds.Phone.NUMBER)
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
        )?.use {
            while (it.moveToNext()) {
                when (it.getInt(it.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE))) {
                    ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE -> {
                        email = it.getString(ContactsContract.CommonDataKinds.Email.LABEL)
                    }
                    else -> {
                        email2 = it.getString(ContactsContract.CommonDataKinds.Email.LABEL)
                    }
                }
            }
        }

        return Contact(
            rowId,
            lookupKey,
            R.drawable.ic_contact,
            name,
            phone, phone2,
            email, email2,
            "",
            0,
        )
    }

    private fun Cursor.getString(columnName: String): String {
        return getString(getColumnIndex(columnName))
    }
}
