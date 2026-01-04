package com.vaspit.simplesmsforwarder.core.presentation

data class ButtonState(
    val isLoading: Boolean = false,
    val isEnabled: Boolean = true,
    val text: UiText,
)