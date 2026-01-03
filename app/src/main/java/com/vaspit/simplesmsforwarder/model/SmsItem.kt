package com.vaspit.simplesmsforwarder.model

import kotlinx.serialization.Serializable

@Serializable
data class SmsItem(
    val content: String,
    val sender: String,
    val packageName: String,
    val timestamp: Long,
    var isSent: Boolean = false,
)