package com.vaspit.simplesmsforwarder.settings.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class SmsItem(
    val content: String,
    val sender: String,
    val packageName: String,
    val timestamp: Long,
    var isSent: Boolean = false,
)