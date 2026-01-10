package com.vaspit.simplesmsforwarder.forwarding.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "sms",
    indices = [Index(value = ["uniqueKey"], unique = true)]
)
data class SmsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val uniqueKey: String,
    val sender: String?,
    val content: String,
    val timestamp: Long,
    val isSent: Boolean = false,
    val lastError: String? = null,
)
