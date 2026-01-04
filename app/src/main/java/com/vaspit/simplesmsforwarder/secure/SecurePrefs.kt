package com.vaspit.simplesmsforwarder.secure

import android.content.Context
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

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

    fun getTelegramToken(): String {
        return prefs.getString(KEY_TOKEN, "") ?: ""
    }

    fun getTelegramId(): String {
        return prefs.getString(KEY_ID, "") ?: ""
    }

    companion object {
        private const val KEY_TOKEN = "telegram_token"
        private const val KEY_ID = "telegram_id"
    }
}