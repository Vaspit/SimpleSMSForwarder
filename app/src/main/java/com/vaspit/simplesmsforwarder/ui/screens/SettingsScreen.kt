package com.vaspit.simplesmsforwarder.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.vaspit.simplesmsforwarder.R
import com.vaspit.simplesmsforwarder.settings.presentation.SettingsScreenEvent
import com.vaspit.simplesmsforwarder.settings.presentation.SettingsScreenState
import com.vaspit.simplesmsforwarder.ui.components.SMSForwarderButton
import com.vaspit.simplesmsforwarder.ui.components.SettingsItem

@Composable
fun SettingsScreen(
    state: SettingsScreenState,
    onEvent: (SettingsScreenEvent) -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { innerPaddings ->
        Column(
            Modifier.fillMaxWidth()
        ) {
            SettingsFields(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPaddings)
                    .padding(16.dp),
                telegramToken = state.telegramToken,
                telegramId = state.telegramId,
                onEvent = onEvent,
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
                }
            )
        }
    }
}

@Composable
private fun SettingsFields(
    modifier: Modifier,
    telegramToken: TextFieldValue,
    telegramId: TextFieldValue,
    onEvent: (SettingsScreenEvent) -> Unit,
) {
    LazyColumn(
        modifier = modifier,
        state = rememberLazyListState(),
    ) {
        item {
            SettingsItem(
                title = stringResource(R.string.settings_screen_telegram_token_title),
                value = telegramToken,
                onValueChange = { newValue ->
                    onEvent(SettingsScreenEvent.TelegramTokenValueChanged(newValue))
                },
            )
        }
        item {
            SettingsItem(
                title = stringResource(R.string.settings_screen_telegram_id_title),
                value = telegramId,
                onValueChange = { newValue ->
                    onEvent(SettingsScreenEvent.TelegramIdValueChanged(newValue))
                },
            )
        }
    }
}