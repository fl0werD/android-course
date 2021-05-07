package com.example.fl0wer

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.fl0wer.Const.NOTICE_BIRTHDAY_EXTRA_CONTACT_ID
import com.example.fl0wer.Const.NOTIFICATION_CHANNEL_ID
import com.example.fl0wer.Const.RECEIVER_INTENT_ACTION_CONTACT_BIRTHDAY
import com.example.fl0wer.contacts.ContactsInteractor
import com.example.fl0wer.dispatchers.DispatchersProvider
import com.example.fl0wer.presentation.main.MainActivity
import javax.inject.Inject
import kotlinx.coroutines.*

class ContactBroadcastReceiver : BroadcastReceiver() {
    @Inject
    lateinit var appScope: CoroutineScope
    @Inject
    lateinit var dispatchersProvider: DispatchersProvider
    @Inject
    lateinit var contactsInteractor: ContactsInteractor

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) {
            return
        }
        (context as App).appComponent.inject(this)
        if (intent.action == RECEIVER_INTENT_ACTION_CONTACT_BIRTHDAY) {
            val contactId = intent.getStringExtra(NOTICE_BIRTHDAY_EXTRA_CONTACT_ID)

            requireNotNull(contactId) {
                "Contact id argument required"
            }

            sendBirthdayNotification(context, contactId)
        }
    }

    private fun sendBirthdayNotification(context: Context, contactId: String) {
        appScope.launch(dispatchersProvider.default) {
            val contact = contactsInteractor.contact(contactId) ?: return@launch

            val pendingIntent = Intent(context, MainActivity::class.java).let {
                it.putExtra(NOTICE_BIRTHDAY_EXTRA_CONTACT_ID, contactId)
                PendingIntent.getActivity(context, 0, it, FLAG_ONE_SHOT)
            }
            NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_contact)
                .setContentText(context.getString(R.string.birthday_notice, contact.name))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()
                .also {
                    context.sendNotification(contact.id, it)
                }
        }
    }
}
