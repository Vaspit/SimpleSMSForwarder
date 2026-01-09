package com.vaspit.simplesmsforwarder.settings.presentation

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewModelScope
import com.vaspit.simplesmsforwarder.R
import com.vaspit.simplesmsforwarder.core.presentation.BaseViewModel
import com.vaspit.simplesmsforwarder.secure.SecurePrefsManager
import com.vaspit.simplesmsforwarder.settings.domain.model.SmsForwardingSettings
import com.vaspit.simplesmsforwarder.settings.domain.usecase.GetIsSettingsEnteredUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsScreenViewModel(
    private val securePrefsManager: SecurePrefsManager,
    private val getIsSettingsEnteredUseCase: GetIsSettingsEnteredUseCase,
) : BaseViewModel<SettingsScreenEvent>() {

    var state = MutableStateFlow(
        SettingsScreenState()
    )
        private set

    val sideEffects = MutableSharedFlow<SettingsScreenSideEffect>()

    init {
        subscribeOnTextFields()
        subscribeOnParametersEntered()
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

    private fun subscribeOnTextFields() {
        viewModelScope.launch {
            state.collectLatest {
                val token = it.telegramToken.text
                val id = it.telegramId.text

                state.update { oldState ->
                    oldState.copy(
                        buttonState = oldState.buttonState.copy(
                            isEnabled = token.isNotBlank() && id.isNotBlank()
                        )
                    )
                }
            }
        }
    }

    private fun subscribeOnParametersEntered() {
        viewModelScope.launch {
            getIsSettingsEnteredUseCase.invoke().collectLatest { isEntered ->
                state.update { oldState ->
                    oldState.copy(
                        isForwarderReady = isEntered
                    )
                }
            }
        }
    }

    private fun onTelegramIdValueChanged(newValue: TextFieldValue) {
        state.update { state ->
            state.copy(telegramId = newValue)
        }
    }

    private fun onTelegramTokenValueChanged(newValue: TextFieldValue) {
        state.update { state ->
            state.copy(telegramToken = newValue)
        }
    }

    private fun onSaveClicked() {
        viewModelScope.launch {
            state.update { oldState ->
                oldState.copy(
                    buttonState = oldState.buttonState.copy(
                        isLoading = true
                    )
                )
            }

            val result = runCatching {
                withContext(Dispatchers.IO) {
                    securePrefsManager.saveSettings(
                        settings = SmsForwardingSettings(
                            telegramToken = state.value.telegramToken.text,
                            telegramUserId = state.value.telegramId.text,
                        ),
                    )
                }
            }

            state.update { oldState ->
                oldState.copy(
                    telegramId = TextFieldValue(),
                    telegramToken = TextFieldValue(),
                    buttonState = oldState.buttonState.copy(
                        isLoading = false,
                    )
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