package com.vaspit.simplesmsforwarder.core.presentation

import android.content.Context

sealed class UiText {

    data class DynamicString(val text: String) : UiText()

    data class StaticString(
        val resourceId: Int,
        val args: List<Any> = emptyList(),
    ) : UiText()

    fun asString(context: Context): String {
        return when (this) {
            is DynamicString -> text
            is StaticString -> context.getString(resourceId, *args.toTypedArray())
        }
    }
}