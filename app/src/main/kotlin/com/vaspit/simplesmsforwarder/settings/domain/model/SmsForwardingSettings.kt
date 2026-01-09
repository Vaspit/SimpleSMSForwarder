package com.vaspit.simplesmsforwarder.settings.domain.model

data class SmsForwardingSettings(
    val telegramUserId: String,
    val telegramToken: String,
)