package com.vaspit.simplesmsforwarder.forwarding

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.vaspit.simplesmsforwarder.forwarding.data.db.AppDatabase
import com.vaspit.simplesmsforwarder.forwarding.data.model.SmsEntity
import com.vaspit.simplesmsforwarder.forwarding.data.worker.SendSmsWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SmsReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return
        if (intent.action != Telephony.Sms.Intents.SMS_RECEIVED_ACTION) return

        val pendingResult = goAsync()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
                var body = ""
                var sender: String? = null
                var timestamp = 0L

                for (message in messages) {
                    sender = message.displayOriginatingAddress
                    timestamp = message.timestampMillis
                    body += message.displayMessageBody
                }

                val uniqueKey = "${sender}_${timestamp}_${body.hashCode()}"
                val db = AppDatabase.get(context)
                db.smsDao().insertIgnore(
                    SmsEntity(
                        uniqueKey = uniqueKey,
                        sender = sender,
                        content = body,
                        timestamp = timestamp
                    )
                )

                enqueueSendWork(context)
            } finally {
                pendingResult.finish()
            }
        }
    }

    private fun enqueueSendWork(context: Context) {
        val request = OneTimeWorkRequestBuilder<SendSmsWorker>().build()
        WorkManager.getInstance(context).enqueueUniqueWork(
            "send_unsent_sms",
            ExistingWorkPolicy.KEEP,
            request
        )
    }
}