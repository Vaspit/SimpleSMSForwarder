package com.vaspit.simplesmsforwarder

import android.app.Activity
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object SmsPermissionManager {

    const val PERMISSIONS_REQUEST_CODE = 1

    private val permissions = mutableListOf<String>().apply {
        add(android.Manifest.permission.INTERNET)
        add(android.Manifest.permission.RECEIVE_SMS)
        add(android.Manifest.permission.READ_SMS)
        // Add permissions conditionally based on API levels
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            add(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            add(android.Manifest.permission.FOREGROUND_SERVICE)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            add(android.Manifest.permission.QUERY_ALL_PACKAGES)
        }
        // Foreground service special use is only available on Android 14 and later
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            add(android.Manifest.permission.FOREGROUND_SERVICE_SPECIAL_USE)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            add(android.Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    fun requestPermissions(activity: Activity) {
        val permissionsToRequest = mutableListOf<String>()
        val permissionsToShowRationale = mutableListOf<String>()

        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission)
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                    permissionsToShowRationale.add(permission)
                }
            }
        }

        if (permissionsToRequest.isNotEmpty()) {
            if (permissionsToShowRationale.isNotEmpty()) {
                // Show rationale dialog if needed
                showRationaleDialog(activity, permissionsToRequest.toTypedArray())
            } else {
                // Request permissions directly
                ActivityCompat.requestPermissions(activity, permissionsToRequest.toTypedArray(), PERMISSIONS_REQUEST_CODE)
            }
        }
    }

    private fun showRationaleDialog(activity: Activity, permissionsToRequest: Array<String>) {
        AlertDialog.Builder(activity)
            .setTitle("Permissions Required")
            .setMessage("This app needs the following permissions to function properly: ${permissionsToRequest.joinToString(", ")}")
            .setPositiveButton("OK") { _, _ ->
                // Request permissions after showing rationale
                ActivityCompat.requestPermissions(activity, permissionsToRequest, PERMISSIONS_REQUEST_CODE)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
                Toast.makeText(activity, "Permissions are required for the app to work properly.", Toast.LENGTH_LONG).show()
            }
            .create()
            .show()
    }
}