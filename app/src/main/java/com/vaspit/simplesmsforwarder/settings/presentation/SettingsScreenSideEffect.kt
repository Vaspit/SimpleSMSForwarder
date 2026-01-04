package com.vaspit.simplesmsforwarder.settings.presentation

sealed interface SettingsScreenSideEffect {
    data class ShowToast(val resId: Int) : SettingsScreenSideEffect
}