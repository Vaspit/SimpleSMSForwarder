package com.vaspit.simplesmsforwarder.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.vaspit.simplesmsforwarder.R
import com.vaspit.simplesmsforwarder.core.presentation.ButtonState
import com.vaspit.simplesmsforwarder.core.presentation.UiText
import com.vaspit.simplesmsforwarder.settings.presentation.SettingsScreenEvent
import com.vaspit.simplesmsforwarder.settings.presentation.SettingsScreenState
import com.vaspit.simplesmsforwarder.settings.presentation.settingsfields.SettingsFieldsState
import com.vaspit.simplesmsforwarder.ui.components.SMSForwarderButton
import com.vaspit.simplesmsforwarder.ui.components.SettingsFragment
import com.vaspit.simplesmsforwarder.ui.components.SettingsItem
import com.vaspit.simplesmsforwarder.ui.theme.SimpleSMSForwarderTheme

@Composable
fun SettingsScreen(
    state: SettingsScreenState,
    onEvent: (SettingsScreenEvent) -> Unit,
) {
    val tokenFocusRequester = remember { FocusRequester() }
    val userIdFocusRequester = remember { FocusRequester() }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { innerPaddings ->
        Column(
            Modifier.fillMaxWidth(),
        ) {
            SettingsFields(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPaddings)
                    .padding(16.dp),
                state = state.settingsFieldsState,
                tokenFocusRequester = tokenFocusRequester,
                userIdFocusRequester = userIdFocusRequester,
                onEvent = onEvent,
            )
            Spacer(modifier = Modifier.weight(1f))
            SettingsFragment(
                modifier = Modifier.fillMaxWidth(),
                isForwarderReady = state.isForwarderReady,
            )
            Spacer(modifier = Modifier.weight(1f))
            SMSForwarderButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPaddings)
                    .padding(16.dp),
                buttonState = state.buttonState,
                onClick = {
                    onEvent(SettingsScreenEvent.OnSaveClicked)
                },
            )
        }
    }
}

@Composable
private fun SettingsFields(
    modifier: Modifier,
    state: SettingsFieldsState,
    tokenFocusRequester: FocusRequester,
    userIdFocusRequester: FocusRequester,
    onEvent: (SettingsScreenEvent) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        SettingsItem(
            title = stringResource(R.string.settings_screen_telegram_token_title),
            value = state.telegramToken,
            placeholder = stringResource(R.string.settings_screen_telegram_token_placeholder),
            focusRequester = tokenFocusRequester,
            onValueChange = { onEvent(SettingsScreenEvent.TelegramTokenValueChanged(it)) },
            onClearTextClick = { onEvent(SettingsScreenEvent.OnClearTelegramTokenClicked) },
            imeAction = ImeAction.Next,
            onImeAction = {
                userIdFocusRequester.requestFocus()
            },
        )
        SettingsItem(
            title = stringResource(R.string.settings_screen_telegram_id_title),
            value = state.telegramId,
            placeholder = stringResource(R.string.settings_screen_telegram_id_placeholder),
            focusRequester = userIdFocusRequester,
            onValueChange = { onEvent(SettingsScreenEvent.TelegramIdValueChanged(it)) },
            onClearTextClick = { onEvent(SettingsScreenEvent.OnClearTelegramIdClicked) },
            imeAction = ImeAction.Done,
            onImeAction = {
                focusManager.clearFocus(force = true)
                keyboardController?.hide()
            },
        )
    }
}

@PreviewLightDark
@Composable
private fun SettingsScreenPreview() {
    SimpleSMSForwarderTheme {
        val settingsFieldsState = SettingsFieldsState(
            telegramToken = TextFieldValue("siufhbfiusebfuisebfuiesbfuse"),
            telegramId = TextFieldValue("1284817491824"),
        )
        val state = SettingsScreenState(
            settingsFieldsState = settingsFieldsState,
            buttonState = ButtonState(text = UiText.DynamicString("Save")),
            isForwarderReady = true,
        )
        SettingsScreen(
            state = state,
        ) { }
    }
}