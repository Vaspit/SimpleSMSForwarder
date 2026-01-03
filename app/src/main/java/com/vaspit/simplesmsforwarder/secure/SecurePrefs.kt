package com.vaspit.simplesmsforwarder.secure

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class SecurePrefsManager(context: Context) {

    private val mutex = Mutex()
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

    suspend fun saveTelegramToken(token: String) {
        mutex.withLock {
            prefs.edit().putString(KEY_TOKEN, token).apply()
        }
    }

    suspend fun saveTelegramId(id: String) {
        mutex.withLock {
            prefs.edit().putString(KEY_ID, id).apply()
        }
    }

    suspend fun getTelegramToken(): String {
        return mutex.withLock {
            prefs.getString(KEY_TOKEN, "") ?: ""
        }
    }

    suspend fun getTelegramId(): String {
        return mutex.withLock {
            prefs.getString(KEY_ID, "") ?: ""
        }
    }

    companion object {
        private const val KEY_TOKEN = "telegram_token"
        private const val KEY_ID = "telegram_id"
    }
}