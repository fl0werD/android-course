package com.example.fl0wer.androidApp.data.contacts

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.fl0wer.androidApp.domain.contacts.Contact
import com.example.fl0wer.androidApp.domain.contacts.ReminderRepository
import com.example.fl0wer.androidApp.ui.core.dispatchers.DispatchersProvider
import com.example.fl0wer.androidApp.util.Const
import com.example.fl0wer.androidApp.util.getAlarmManager
import com.example.fl0wer.androidApp.util.setTimer
import java.util.GregorianCalendar
import kotlinx.coroutines.withContext

class ReminderRepositoryImpl(
    private val context: Context,
    private val dispatchersProvider: DispatchersProvider,
) : ReminderRepository {
    override suspend fun birthdayReminder(contact: Contact) =
        withContext(dispatchersProvider.io) {
            getBirthdayIntent(contact) != null
        }

    override suspend fun addBirthdayReminder(contact: Contact, nextBirthday: GregorianCalendar) =
        withContext(dispatchersProvider.io) {
            val alarmManager = getAlarmManager(context) ?: return@withContext
            val birthdayNotice = createBirthdayIntent(contact) ?: return@withContext
            alarmManager.setTimer(AlarmManager.RTC, nextBirthday.timeInMillis, birthdayNotice)
        }

    override suspend fun removeBirthdayReminder(contact: Contact) =
        withContext(dispatchersProvider.io) {
            val alarmManager = getAlarmManager(context) ?: return@withContext
            val birthdayNotice = getBirthdayIntent(contact) ?: return@withContext
            alarmManager.cancel(birthdayNotice)
            birthdayNotice.cancel()
        }

    private fun getBirthdayIntent(contact: Contact) =
        PendingIntent.getBroadcast(
            context,
            contact.id,
            Intent(Const.RECEIVER_INTENT_ACTION_CONTACT_BIRTHDAY),
            PendingIntent.FLAG_NO_CREATE,
        )

    private fun createBirthdayIntent(contact: Contact) =
        Intent(Const.RECEIVER_INTENT_ACTION_CONTACT_BIRTHDAY).let {
            it.putExtra(Const.NOTICE_BIRTHDAY_EXTRA_CONTACT_ID, contact.lookupKey)
            PendingIntent.getBroadcast(
                context,
                contact.id,
                it,
                PendingIntent.FLAG_UPDATE_CURRENT,
            )
        }
}
