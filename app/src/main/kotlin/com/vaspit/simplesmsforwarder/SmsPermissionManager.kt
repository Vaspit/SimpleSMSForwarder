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
            .setTitle(activity.getString(R.string.permissions_dialog_title))
            .setMessage("This app needs the following permissions to function properly: ${permissionsToRequest.joinToString(", ")}")
            .setPositiveButton(activity.getString(R.string.permissions_dialog_positive_button)) { _, _ ->
                ActivityCompat.requestPermissions(activity, permissionsToRequest, PERMISSIONS_REQUEST_CODE)
            }
            .setNegativeButton(activity.getString(R.string.permissions_dialog_negative_button)) { dialog, _ ->
                dialog.dismiss()
                Toast.makeText(
                    activity,
                    activity.getString(R.string.permissions_dialog_negative_hint),
                    Toast.LENGTH_LONG,
                ).show()
            }
            .create()
            .show()
    }
}