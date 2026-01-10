package com.vaspit.simplesmsforwarder.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vaspit.simplesmsforwarder.settings.domain.usecase.GetIsSettingsEnteredUseCase
import com.vaspit.simplesmsforwarder.settings.domain.usecase.SaveSettingsUseCase

class SettingsScreenViewModelFactory(
    private val getIsSettingsEnteredUseCase: GetIsSettingsEnteredUseCase,
    private val saveSettingsUseCase: SaveSettingsUseCase,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsScreenViewModel::class.java)) {
            return SettingsScreenViewModel(
                getIsSettingsEnteredUseCase = getIsSettingsEnteredUseCase,
                saveSettingsUseCase = saveSettingsUseCase,
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}