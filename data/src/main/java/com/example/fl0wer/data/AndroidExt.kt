package com.example.fl0wer.data

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build

fun getAlarmManager(context: Context): AlarmManager? =
    (context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?)

fun AlarmManager.setTimer(type: Int, triggerAtMillis: Long, operation: PendingIntent) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        setExactAndAllowWhileIdle(type, triggerAtMillis, operation)
    } else {
        setExact(type, triggerAtMillis, operation)
    }
}
