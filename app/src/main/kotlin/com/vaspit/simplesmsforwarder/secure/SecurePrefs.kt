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

    fun saveSettings(settings: SmsForwardingSettings) {
        val telegramToken = settings.telegramToken
        val telegramUserId = settings.telegramUserId

        prefs.edit { putString(KEY_TOKEN, telegramToken) }
        prefs.edit { putString(KEY_USER_ID, telegramUserId) }
    }

    private fun getSettings(): SmsForwardingSettings {
        val telegramUserId = prefs.getString(KEY_USER_ID, "") ?: ""
        val telegramToken = prefs.getString(KEY_TOKEN, "") ?: ""

        return SmsForwardingSettings(
            telegramToken = telegramToken,
            telegramUserId = telegramUserId,
        )
    }

    fun settingsFlow(): Flow<SmsForwardingSettings> = callbackFlow {
        fun emitCurrent() {
            trySend(getSettings()).isSuccess
        }

        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == KEY_TOKEN || key == KEY_USER_ID) {
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
        private const val KEY_USER_ID = "telegram_user_id"
    }
}