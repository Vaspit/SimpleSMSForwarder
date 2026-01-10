package com.vaspit.simplesmsforwarder

import android.app.Activity
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object SmsPermissionManager {

    const val PERMISSIONS_REQUEST_CODE = 1

    private val permissions = mutableListOf<String>().apply {
        add(android.Manifest.permission.RECEIVE_SMS)
        add(android.Manifest.permission.READ_SMS)
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
                showRationaleDialog(activity, permissionsToRequest.toTypedArray())
            } else {
                ActivityCompat.requestPermissions(activity, permissionsToRequest.toTypedArray(), PERMISSIONS_REQUEST_CODE)
            }
        }
    }

    private fun showRationaleDialog(activity: Activity, permissionsToRequest: Array<String>) {
        AlertDialog.Builder(activity)
            .setTitle("Permissions Required")
            .setMessage("This app needs the following permissions to function properly: ${permissionsToRequest.joinToString(", ")}")
            .setPositiveButton("OK") { _, _ ->
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