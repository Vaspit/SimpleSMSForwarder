package com.vaspit.simplesmsforwarder.secure

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.vaspit.simplesmsforwarder.settings.domain.model.SmsForwardingSettings
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

class SecurePrefsManager(context: Context) {

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val prefs = EncryptedSharedPreferences.create(
        context,
        "secure_settings",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
    )

    fun saveTelegramToken(token: String) {
        return prefs.edit { putString(KEY_TOKEN, token) }
    }

    fun saveTelegramId(id: String) {
        return prefs.edit { putString(KEY_ID, id) }
    }

    private fun getTelegramToken(): String {
        return prefs.getString(KEY_TOKEN, "") ?: ""
    }

    private fun getTelegramId(): String {
        return prefs.getString(KEY_ID, "") ?: ""
    }

    fun saveSettings(settings: SmsForwardingSettings) {
        val telegramToken = settings.telegramToken
        val telegramUserId = settings.telegramUserId

        saveTelegramToken(telegramToken)
        saveTelegramId(telegramUserId)
    }

    fun getSettings(): SmsForwardingSettings {
        val telegramId = getTelegramId()
        val telegramToken = getTelegramToken()

        return SmsForwardingSettings(
            telegramToken = telegramToken,
            telegramUserId = telegramId,
        )
    }

    fun settingsFlow(): Flow<SmsForwardingSettings> = callbackFlow {
        fun emitCurrent() {
            trySend(getSettings()).isSuccess
        }

        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == KEY_TOKEN || key == KEY_ID) {
                emitCurrent()
            }
        }

        emitCurrent()

        prefs.registerOnSharedPreferenceChangeListener(listener)

        awaitClose {
            prefs.unregisterOnSharedPreferenceChangeListener(listener)
        }
    }.distinctUntilChanged()

    companion object {
        private const val KEY_TOKEN = "telegram_token"
        private const val KEY_ID = "telegram_id"
    }
}