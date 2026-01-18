package com.vaspit.simplesmsforwarder.settings.presentation.settingsfields

import androidx.compose.ui.text.input.TextFieldValue

data class SettingsFieldsState(
    val telegramToken: TextFieldValue = TextFieldValue(),
    val telegramId: TextFieldValue = TextFieldValue(),
)
