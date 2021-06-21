package com.example.fl0wer.androidApp.data.contacts

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.fl0wer.androidApp.util.Const
import com.example.fl0wer.domain.contacts.Contact
import com.example.fl0wer.domain.contacts.ReminderRepository
import com.example.fl0wer.androidApp.getAlarmManager
import com.example.fl0wer.androidApp.setTimer
import java.util.GregorianCalendar

class ReminderRepositoryImpl(
    private val context: Context,
) : ReminderRepository {
    override fun birthdayReminder(contact: Contact) =
        getBirthdayIntent(contact) != null

    override fun addBirthdayReminder(contact: Contact, nextBirthday: GregorianCalendar) {
        val alarmManager = getAlarmManager(context) ?: return
        val birthdayNotice = createBirthdayIntent(contact) ?: return
        alarmManager.setTimer(AlarmManager.RTC, nextBirthday.timeInMillis, birthdayNotice)
    }

    override fun removeBirthdayReminder(contact: Contact) {
        val alarmManager = getAlarmManager(context) ?: return
        val birthdayNotice = getBirthdayIntent(contact) ?: return
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
