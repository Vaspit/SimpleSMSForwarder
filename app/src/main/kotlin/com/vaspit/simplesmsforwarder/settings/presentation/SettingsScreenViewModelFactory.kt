package com.vaspit.simplesmsforwarder.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vaspit.simplesmsforwarder.secure.SecurePrefsManager

class SettingsScreenViewModelFactory(
    private val securePrefsManager: SecurePrefsManager,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsScreenViewModel::class.java)) {
            return SettingsScreenViewModel(securePrefsManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}