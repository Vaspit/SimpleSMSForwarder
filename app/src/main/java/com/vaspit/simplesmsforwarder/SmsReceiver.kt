package com.vaspit.simplesmsforwarder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.util.Log
import com.vaspit.simplesmsforwarder.settings.domain.model.SmsItem

class SmsReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
            var resultContent = ""
            var resultSender: String? = null
            var resultTimeStamp = 0L

            for (message in messages) {
                resultSender = message.displayOriginatingAddress
                resultTimeStamp = message.timestampMillis
                resultContent += message.displayMessageBody
            }

            val message = SmsItem(
                content = resultContent,
                sender = "SMS from $resultSender",
                packageName = "SMS message",
                timestamp = resultTimeStamp
            )

            if (!SmsQueue.containsMessage(message)) {
                Log.d("SmsReceiver", "SMS received from ${message.sender}: ${message.content}")
                SmsQueue.messages.add(message)
            }
        }
    }
}