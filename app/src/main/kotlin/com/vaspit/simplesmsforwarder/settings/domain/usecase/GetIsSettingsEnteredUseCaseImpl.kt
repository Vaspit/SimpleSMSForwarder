package com.vaspit.simplesmsforwarder.settings.domain.usecase

import com.vaspit.simplesmsforwarder.settings.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetIsSettingsEnteredUseCaseImpl(
    private val settingsRepository: SettingsRepository
) : GetIsSettingsEnteredUseCase {

    override fun invoke(): Flow<Boolean> {
        return settingsRepository.settingsFlow().map { settings ->
            settings.telegramToken.isNotBlank() && settings.telegramUserId.isNotBlank()
        }
    }
}