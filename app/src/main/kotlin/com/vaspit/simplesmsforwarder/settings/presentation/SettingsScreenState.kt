package com.vaspit.simplesmsforwarder.settings.presentation

import com.vaspit.simplesmsforwarder.R
import com.vaspit.simplesmsforwarder.core.presentation.ButtonState
import com.vaspit.simplesmsforwarder.core.presentation.UiText
import com.vaspit.simplesmsforwarder.settings.presentation.settingsfields.SettingsFieldsState

data class SettingsScreenState(
    val settingsFieldsState: SettingsFieldsState = SettingsFieldsState(),
    val buttonState: ButtonState = ButtonState(
        text = UiText.StaticString(R.string.settings_screen_save_button),
    ),
    val isForwarderReady: Boolean = false,
)