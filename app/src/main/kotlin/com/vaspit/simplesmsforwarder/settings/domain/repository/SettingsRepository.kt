package com.vaspit.simplesmsforwarder.settings.domain.repository

import com.vaspit.simplesmsforwarder.settings.domain.model.SmsForwardingSettings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun saveSettings(settings: SmsForwardingSettings)
    fun settingsFlow(): Flow<SmsForwardingSettings>
}