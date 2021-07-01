package com.example.fl0wer.androidApp.util

import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import com.example.fl0wer.R
import com.example.fl0wer.androidApp.data.directions.LatLonParcelable
import com.example.fl0wer.androidApp.data.directions.network.DirectionsResponse
import com.example.fl0wer.androidApp.data.locations.network.GeocodeResponse
import com.example.fl0wer.androidApp.util.Const.NOTIFICATION_CHANNEL_ID
import com.example.fl0wer.androidApp.util.Const.RESPONSE_SUCCESS_STATUS
import com.google.android.gms.maps.model.LatLng

fun DirectionsResponse.isSuccess() = (status == RESPONSE_SUCCESS_STATUS)

fun GeocodeResponse.isSuccess() = (status == RESPONSE_SUCCESS_STATUS)

fun LatLonParcelable.toLatLng() = LatLng(latitude, longitude)

fun getAlarmManager(context: Context): AlarmManager? =
    (context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?)

fun AlarmManager.setTimer(type: Int, triggerAtMillis: Long, operation: PendingIntent) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        setExactAndAllowWhileIdle(type, triggerAtMillis, operation)
    } else {
        setExact(type, triggerAtMillis, operation)
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
