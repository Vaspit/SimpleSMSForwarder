package com.vaspit.simplesmsforwarder.settings.data

import com.vaspit.simplesmsforwarder.secure.SecurePrefsManager
import com.vaspit.simplesmsforwarder.settings.domain.model.SmsForwardingSettings
import com.vaspit.simplesmsforwarder.settings.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow

class SettingsRepositoryImpl(
    private val securePrefsManager: SecurePrefsManager,
) : SettingsRepository {

    override fun saveSettings(settings: SmsForwardingSettings) {
        securePrefsManager.saveSettings(settings)
    }

    override fun settingsFlow(): Flow<SmsForwardingSettings> {
        return securePrefsManager.settingsFlow()
    }
}
