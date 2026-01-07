package com.vaspit.simplesmsforwarder.core.data.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.vaspit.simplesmsforwarder.core.data.db.AppDatabase
import com.vaspit.simplesmsforwarder.secure.SecurePrefsManager
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class SendSmsWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val db = AppDatabase.get(applicationContext)
        val dao = db.smsDao()
        val secure = SecurePrefsManager(applicationContext)

        val telegramToken = secure.getTelegramToken()
        val telegramUserId = secure.getTelegramId()

        if (telegramToken.isEmpty() || telegramUserId.isEmpty()) {
            Log.d("SendSmsWorker", "Error! Token or telegram user ID is empty.")
            return Result.success()
        }

        val unsent = dao.getUnsent(limit = 20)

        if (unsent.isEmpty()) {
            Log.d("SendSmsWorker", "Nothing to send.")
            return Result.success()
        }

        for (sms in unsent) {
            try {
                val ok = sendMessage(telegramToken, telegramUserId, sms.content)
                if (ok) {
                    dao.markSent(sms.id)
                } else {
                    Log.d("SendSmsWorker", "Failure! The message didn't send.")
                    return Result.retry()
                }
            } catch (t: Throwable) {
                Log.d("SendSmsWorker", "Error! ${t.message}")
                return Result.retry()
            }
        }

        Log.d("SendSmsWorker", "Success! The message sent.")

        return Result.success()
    }

    private fun sendMessage(telegramToken: String, telegramUserId: String, content: String): Boolean {
        val urlString = "https://api.telegram.org/bot$telegramToken/sendMessage"
        val messageText = "<b>${content}</b>\n<blockquote></blockquote>"
        val params = "chat_id=$telegramUserId&parse_mode=HTML&text=${URLEncoder.encode(messageText, "UTF-8")}"

        val url = URL(urlString)
        val connection = (url.openConnection() as HttpURLConnection).apply {
            requestMethod = "POST"
            doOutput = true
            setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
            connectTimeout = 15_000
            readTimeout = 15_000
        }

        connection.outputStream.use { out ->
            out.write(params.toByteArray())
            out.flush()
        }

        val code = connection.responseCode
        connection.disconnect()
        return code == 200
    }
}