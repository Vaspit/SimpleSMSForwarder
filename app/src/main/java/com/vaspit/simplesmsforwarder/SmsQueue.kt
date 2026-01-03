package com.vaspit.simplesmsforwarder

import com.vaspit.simplesmsforwarder.settings.domain.model.SmsItem
import java.util.concurrent.ConcurrentLinkedQueue

object SmsQueue {

    val messages: ConcurrentLinkedQueue<SmsItem> = ConcurrentLinkedQueue()

    fun containsMessage(message: SmsItem): Boolean {
        if (messages.any { it.timestamp == message.timestamp }) return true
        return messages.any { it.content == message.content && it.sender == message.sender }
    }
}