package com.vaspit.simplesmsforwarder

import android.app.Application
import com.vaspit.simplesmsforwarder.secure.SecurePrefsManager
import com.vaspit.simplesmsforwarder.settings.data.SettingsRepositoryImpl
import com.vaspit.simplesmsforwarder.settings.domain.repository.SettingsRepository

class App : Application() {

    lateinit var securePrefsManager: SecurePrefsManager
    lateinit var settingsRepository: SettingsRepository

    override fun onCreate() {
        super.onCreate()
        securePrefsManager = SecurePrefsManager(this)
        settingsRepository = SettingsRepositoryImpl(securePrefsManager)
    }
}