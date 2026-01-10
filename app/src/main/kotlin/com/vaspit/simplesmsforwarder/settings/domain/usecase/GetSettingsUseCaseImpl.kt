package com.vaspit.simplesmsforwarder.settings.domain.usecase

import com.vaspit.simplesmsforwarder.settings.domain.model.SmsForwardingSettings
import com.vaspit.simplesmsforwarder.settings.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.first

class GetSettingsUseCaseImpl(
    private val settingsRepository: SettingsRepository,
) : GetSettingsUseCase {

    override suspend fun invoke(): SmsForwardingSettings {
        return settingsRepository.settingsFlow().first()
    }
}