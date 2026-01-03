package com.vaspit.simplesmsforwarder.settings.presentation

import androidx.compose.ui.text.input.TextFieldValue

sealed interface SettingsScreenEvent {
    data object OnSaveClicked : SettingsScreenEvent
    data class TelegramTokenValueChanged(val newValue: TextFieldValue) : SettingsScreenEvent
    data class TelegramIdValueChanged(val newValue: TextFieldValue) : SettingsScreenEvent
}