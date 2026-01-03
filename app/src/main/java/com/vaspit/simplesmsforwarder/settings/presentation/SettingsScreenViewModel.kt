package com.vaspit.simplesmsforwarder.settings.presentation

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewModelScope
import com.vaspit.simplesmsforwarder.core.presentation.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsScreenViewModel : BaseViewModel<SettingsScreenEvent>() {

    var state = MutableStateFlow(
        SettingsScreenState()
    )
        private set

    init {
        subscribeOnTextFields()
    }

    private fun subscribeOnTextFields() {
        viewModelScope.launch {
            state.collectLatest {
                val token = it.telegramToken.text
                val id = it.telegramId.text

                state.update { oldState ->
                    oldState.copy(
                        isSaveButtonEnabled = token.isNotBlank() && id.isNotBlank(),
                    )
                }
            }
        }
    }

    override fun onEvent(event: SettingsScreenEvent) {
        when (event) {
            is SettingsScreenEvent.TelegramIdValueChanged -> {
                onTelegramIdValueChanged(event.newValue)
            }

            is SettingsScreenEvent.TelegramTokenValueChanged -> {
                onTelegramTokenValueChanged(event.newValue)
            }

            is SettingsScreenEvent.OnSaveClicked -> {
                onSaveClicked()
            }
        }
    }

    private fun onTelegramIdValueChanged(newValue: TextFieldValue) {
        state.update { state ->
            state.copy(
                telegramId = newValue,
            )
        }
    }

    private fun onTelegramTokenValueChanged(newValue: TextFieldValue) {
        state.update { state ->
            state.copy(
                telegramToken = newValue,
            )
        }
    }

    private fun onSaveClicked() {
        viewModelScope.launch {

        }
    }
}