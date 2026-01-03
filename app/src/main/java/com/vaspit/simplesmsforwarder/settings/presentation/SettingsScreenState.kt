package com.vaspit.simplesmsforwarder.settings.presentation

import androidx.compose.ui.text.input.TextFieldValue

data class SettingsScreenState(
    val telegramToken: TextFieldValue = TextFieldValue(),
    val telegramId: TextFieldValue = TextFieldValue(),
    val isSaveButtonEnabled: Boolean = false,
)