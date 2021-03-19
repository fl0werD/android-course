package com.example.fl0wer

import android.app.*
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import com.example.fl0wer.Const.NOTIFICATION_CHANNEL_ID

fun AlarmManager.setTimer(type: Int, triggerAtMillis: Long, operation: PendingIntent) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        setExactAndAllowWhileIdle(
            type,
            triggerAtMillis,
            operation
        )
    } else {
        setExact(
            type,
            triggerAtMillis,
            operation
        )
    }
}

fun Context.sendNotification(id: Int, notification: Notification) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager?
                ?: throw IllegalStateException("Notification service null")

        if (notificationManager.getNotificationChannel(NOTIFICATION_CHANNEL_ID) == null) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                getString(R.string.notice_birthday_name),
                NotificationManager.IMPORTANCE_DEFAULT,
            )
            notificationManager.createNotificationChannel(channel)
        }
    }
    NotificationManagerCompat.from(this)
        .notify(id, notification)
}
