package com.vaspit.simplesmsforwarder.settings.domain.usecase

import com.vaspit.simplesmsforwarder.secure.SecurePrefsManager
import com.vaspit.simplesmsforwarder.settings.domain.model.SmsForwardingSettings

class SaveSettingsUseCaseImpl(
    private val securePrefsManager: SecurePrefsManager,
) : SaveSettingsUseCase {

    override fun invoke(settings: SmsForwardingSettings) {
        securePrefsManager.saveSettings(settings)
    }
}