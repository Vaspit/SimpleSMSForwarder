package com.vaspit.simplesmsforwarder.settings.domain.usecase

import com.vaspit.simplesmsforwarder.settings.domain.model.SmsForwardingSettings
import com.vaspit.simplesmsforwarder.settings.domain.repository.SettingsRepository

class GetSettingsUseCaseImpl(
    private val settingsRepository: SettingsRepository,
) : GetSettingsUseCase {

    override fun invoke(): SmsForwardingSettings {
        return settingsRepository.getSettings()
    }
}