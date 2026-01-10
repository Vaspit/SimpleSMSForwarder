package com.vaspit.simplesmsforwarder.settings.domain.usecase

import com.vaspit.simplesmsforwarder.settings.domain.model.SmsForwardingSettings

interface SaveSettingsUseCase {
    fun invoke(settings: SmsForwardingSettings)
}