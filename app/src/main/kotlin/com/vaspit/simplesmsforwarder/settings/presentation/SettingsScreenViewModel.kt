package com.vaspit.simplesmsforwarder.settings.presentation

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewModelScope
import com.vaspit.simplesmsforwarder.R
import com.vaspit.simplesmsforwarder.core.presentation.BaseViewModel
import com.vaspit.simplesmsforwarder.settings.domain.model.SmsForwardingSettings
import com.vaspit.simplesmsforwarder.settings.domain.usecase.GetIsSettingsEnteredUseCase
import com.vaspit.simplesmsforwarder.settings.domain.usecase.SaveSettingsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsScreenViewModel(
    private val getIsSettingsEnteredUseCase: GetIsSettingsEnteredUseCase,
    private val saveSettingsUseCase: SaveSettingsUseCase,
) : BaseViewModel<SettingsScreenEvent>() {

    var state = MutableStateFlow(
        SettingsScreenState(),
    )
        private set

    val sideEffects = MutableSharedFlow<SettingsScreenSideEffect>()

    init {
        subscribeOnTextFields()
        subscribeOnIsSettingsEntered()
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

            is SettingsScreenEvent.OnClearTelegramIdClicked -> {
                onClearTelegramId()
            }

            is SettingsScreenEvent.OnClearTelegramTokenClicked -> {
                onClearTelegramToken()
            }
        }
    }

    private fun subscribeOnTextFields() {
        viewModelScope.launch {
            state.collectLatest {
                val settingsFieldsState = it.settingsFieldsState
                val id = settingsFieldsState.telegramId.text
                val token = settingsFieldsState.telegramToken.text

                state.update { oldState ->
                    oldState.copy(
                        buttonState = oldState.buttonState.copy(
                            isEnabled = token.isNotBlank() && id.isNotBlank(),
                        ),
                    )
                }
            }
        }
    }

    private fun subscribeOnIsSettingsEntered() {
        viewModelScope.launch {
            getIsSettingsEnteredUseCase.invoke().collectLatest { isEntered ->
                state.update { oldState ->
                    oldState.copy(
                        isForwarderReady = isEntered,
                    )
                }
            }
        }
    }

    private fun onClearTelegramId() {
        state.update { currentState ->
            currentState.copy(
                settingsFieldsState = currentState.settingsFieldsState.copy(
                    telegramId = TextFieldValue(),
                ),
            )
        }
    }

    private fun onClearTelegramToken() {
        state.update { currentState ->
            currentState.copy(
                settingsFieldsState = currentState.settingsFieldsState.copy(
                    telegramToken = TextFieldValue(),
                ),
            )
        }
    }

    private fun onTelegramIdValueChanged(newValue: TextFieldValue) {
        state.update { currentState ->
            currentState.copy(
                settingsFieldsState = currentState.settingsFieldsState.copy(
                    telegramId = newValue,
                ),
            )
        }
    }

    private fun onTelegramTokenValueChanged(newValue: TextFieldValue) {
        state.update { currentState ->
            currentState.copy(
                settingsFieldsState = currentState.settingsFieldsState.copy(
                    telegramToken = newValue,
                ),
            )
        }
    }

    private fun onSaveClicked() {
        viewModelScope.launch {
            state.update { currentState ->
                currentState.copy(
                    buttonState = currentState.buttonState.copy(
                        isLoading = true,
                    ),
                )
            }

            val result = runCatching {
                withContext(Dispatchers.IO) {
                    val settingsFieldsState = state.value.settingsFieldsState
                    saveSettingsUseCase.invoke(
                        settings = SmsForwardingSettings(
                            telegramToken = settingsFieldsState.telegramToken.text,
                            telegramUserId = settingsFieldsState.telegramId.text,
                        ),
                    )
                }
            }

            state.update { currentState ->
                currentState.copy(
                    buttonState = currentState.buttonState.copy(
                        isLoading = false,
                        isEnabled = true,
                    ),
                )
            }

            if (result.isSuccess) {
                sideEffects.emit(SettingsScreenSideEffect.ShowToast(R.string.settings_screen_telegram_parameters_saved_success))
            } else {
                sideEffects.emit(SettingsScreenSideEffect.ShowToast(R.string.settings_screen_telegram_parameters_saved_fail))
            }
        }
    }
}