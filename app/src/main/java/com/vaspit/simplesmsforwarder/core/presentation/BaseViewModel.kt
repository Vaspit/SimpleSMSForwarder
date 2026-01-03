package com.vaspit.simplesmsforwarder.core.presentation

import androidx.lifecycle.ViewModel

abstract class BaseViewModel<EVENT> : ViewModel() {

    abstract fun onEvent(event: EVENT)
}