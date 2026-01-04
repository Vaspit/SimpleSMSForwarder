package com.vaspit.simplesmsforwarder

import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.provider.Telephony
import android.util.Log
import com.vaspit.simplesmsforwarder.secure.SecurePrefsManager
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.util.concurrent.Executors

class SmsForwardingService : Service() {

    private val handler = Handler(Looper.getMainLooper())
    private val smsReceiver = SmsReceiver()
    private val executor = Executors.newSingleThreadExecutor()
    private lateinit var securePrefsManager: SecurePrefsManager
    private val runnable = object : Runnable {
        override fun run() {
            val message = SmsQueue.messages.peek()

            executor.execute {
                if (message != null && !message.isSent) {
                    val isSent = sendMessage(message.content)

                    if (isSent) {
                        message.isSent = true
                        SmsQueue.messages.poll()
                    } else {
                        Log.e("SmsForwardingService", "Message from ${message.sender} was not sent!")
                    }
                }
            }

            handler.postDelayed(this, 2000L)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        securePrefsManager = SecurePrefsManager(application.applicationContext)
        registerReceiver(smsReceiver, IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            startForeground(
                1,
                SmsNotificationManager.createNotification(this),
                ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
            )
        } else {
            startForeground(
                1,
                SmsNotificationManager.createNotification(this)
            )
        }

        handler.post(runnable)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(smsReceiver)
        handler.removeCallbacks(runnable)
        executor.shutdown()
    }

    private fun sendMessage(content: String): Boolean {
        val telegramToken = securePrefsManager.getTelegramToken()
        val telegramUserId = securePrefsManager.getTelegramId()

        if (telegramToken.isEmpty() || telegramUserId.isEmpty() || !telegramToken.contains(':')) {
            return false
        }

        val urlString = "https://api.telegram.org/bot$telegramToken/sendMessage"
        val messageText = "<b>${content}</b>\n<blockquote></blockquote>"
        val params = "chat_id=$telegramUserId&parse_mode=HTML&text=${URLEncoder.encode(messageText, "UTF-8")}"

        return try {
            val url = URL(urlString)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.doOutput = true
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")

            val outputStream: OutputStream = connection.outputStream
            outputStream.write(params.toByteArray())
            outputStream.flush()
            outputStream.close()

            val responseCode = connection.responseCode
            val bufferedReader = if (responseCode == 200) {
                BufferedReader(InputStreamReader(connection.inputStream))
            } else {
                BufferedReader(InputStreamReader(connection.errorStream))
            }

            val response = bufferedReader.use(BufferedReader::readText)
            bufferedReader.close()

            Log.d("SmsForwardingService", response)

            connection.disconnect()

            responseCode == 200
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}