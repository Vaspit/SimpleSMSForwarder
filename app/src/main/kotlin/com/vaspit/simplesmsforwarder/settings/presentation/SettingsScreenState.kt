package com.vaspit.simplesmsforwarder.settings.presentation

import androidx.compose.ui.text.input.TextFieldValue
import com.vaspit.simplesmsforwarder.R
import com.vaspit.simplesmsforwarder.core.presentation.ButtonState
import com.vaspit.simplesmsforwarder.core.presentation.UiText

data class SettingsScreenState(
    val telegramToken: TextFieldValue = TextFieldValue(),
    val telegramId: TextFieldValue = TextFieldValue(),
    val buttonState: ButtonState = ButtonState(
        text = UiText.StaticString(R.string.settings_screen_save_button)
    ),
    val isForwarderReady: Boolean = false,
)