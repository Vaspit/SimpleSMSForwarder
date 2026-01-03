package com.vaspit.simplesmsforwarder

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

object SmsNotificationManager {

    fun createNotification(packageContext: Context): Notification {
        val intent = Intent(packageContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            packageContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        return NotificationCompat.Builder(packageContext, SMS_NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Listening for SMS")
            .setContentText("The SMSForwarder service is running")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .build()
    }

    fun createNotificationChannel(context: Context) {
        val channel = NotificationChannel(
            SMS_NOTIFICATION_CHANNEL_ID,
            "SimpleSMSForwarder Service",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Channel for SMS Forwarder service notifications"
            setShowBadge(false)
            lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        }

        val manager = context.getSystemService(NotificationManager::class.java)
        manager?.createNotificationChannel(channel)
    }
}