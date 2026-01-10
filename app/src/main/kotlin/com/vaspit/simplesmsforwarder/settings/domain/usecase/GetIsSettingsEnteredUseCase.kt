package com.vaspit.simplesmsforwarder.settings.domain.usecase

import kotlinx.coroutines.flow.Flow

interface GetIsSettingsEnteredUseCase {
    fun invoke(): Flow<Boolean>
}