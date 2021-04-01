package com.example.fl0wer

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.fl0wer.Const.NOTICE_BIRTHDAY_EXTRA_CONTACT_ID
import com.example.fl0wer.Const.NOTICE_BIRTHDAY_EXTRA_TEXT
import com.example.fl0wer.Const.NOTIFICATION_CHANNEL_ID
import com.example.fl0wer.Const.RECEIVER_INTENT_ACTION_CONTACT_BIRTHDAY

class ContactBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) {
            return
        }
        if (intent.action == RECEIVER_INTENT_ACTION_CONTACT_BIRTHDAY) {
            val contactId = intent.getStringExtra(NOTICE_BIRTHDAY_EXTRA_CONTACT_ID)
            val text = intent.getStringExtra(NOTICE_BIRTHDAY_EXTRA_TEXT) ?: return

            requireNotNull(contactId) {
                "Contact id argument required"
            }

            sendBirthdayNotification(context, contactId, text)
        }
    }

    private fun sendBirthdayNotification(context: Context, contactId: String, text: String) {
        val pendingIntent = Intent(context, MainActivity::class.java).let {
            it.putExtra(NOTICE_BIRTHDAY_EXTRA_CONTACT_ID, contactId)
            PendingIntent.getActivity(context, 0, it, FLAG_ONE_SHOT)
        }
        val contact = Contacts.getUserById(contactId) ?: return
        NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_contact)
            .setContentText(text)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
            .also {
                context.sendNotification(contact.rowId, it)
            }
    }
}
