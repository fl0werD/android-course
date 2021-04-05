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
import com.example.fl0wer.dispatchers.DispatchersProvider
import com.example.fl0wer.dispatchers.DispatchersProviderImpl
import com.example.fl0wer.main.MainActivity
import com.example.fl0wer.repository.ContactsRepository
import kotlinx.coroutines.*

class ContactBroadcastReceiver(
    private val appScope: CoroutineScope = AppScope.scope,
    private val dispatchersProvider: DispatchersProvider = DispatchersProviderImpl,
) : BroadcastReceiver() {
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
        appScope.launch(dispatchersProvider.default) {
            val contact = ContactsRepository.get(context).getContactById(contactId) ?: return@launch
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
}
