package com.vaspit.simplesmsforwarder

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vaspit.simplesmsforwarder.secure.SecurePrefsManager
import com.vaspit.simplesmsforwarder.settings.presentation.SettingsScreenSideEffect
import com.vaspit.simplesmsforwarder.settings.presentation.SettingsScreenViewModel
import com.vaspit.simplesmsforwarder.settings.presentation.SettingsScreenViewModelFactory
import com.vaspit.simplesmsforwarder.ui.screens.SettingsScreen
import com.vaspit.simplesmsforwarder.ui.theme.SimpleSMSForwarderTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        SmsNotificationManager.createNotificationChannel(this)
        SmsPermissionManager.requestPermissions(this)

        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            val securePrefs = remember { SecurePrefsManager(context) }
            val viewModel = viewModel<SettingsScreenViewModel>(
                factory = SettingsScreenViewModelFactory(securePrefs)
            )
            val state = viewModel.state.collectAsStateWithLifecycle().value

            LaunchedEffect(Unit) {
                viewModel.sideEffects.collect { sideEffect ->
                    when (sideEffect) {
                        is SettingsScreenSideEffect.ShowToast -> {
                            Toast.makeText(
                                this@MainActivity,
                                context.getString(sideEffect.resId),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }

            SimpleSMSForwarderTheme {
                SettingsScreen(
                    state = state,
                    onEvent = viewModel::onEvent,
                )
            }
        }
    }

    @Deprecated("")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == SmsPermissionManager.PERMISSIONS_REQUEST_CODE) {
            val permissionsToRequest = mutableListOf<String>()

            for ((index, permission) in permissions.withIndex()) {
                if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {
                    permissionsToRequest.add(permission)
                }
            }

            if (permissionsToRequest.isNotEmpty()) {
                Toast.makeText(
                    this,
                    "Some permissions were not granted. This may affect operation.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}