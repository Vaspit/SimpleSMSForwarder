package com.vaspit.simplesmsforwarder.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.vaspit.simplesmsforwarder.R

@Composable
fun SettingsFragment(
    modifier: Modifier = Modifier,
    isForwarderReady: Boolean,
) {
    val icon = if (isForwarderReady) {
        painterResource(R.drawable.ic_forwarding_ready)
    } else {
        painterResource(R.drawable.ic_forwarding_not_ready)
    }

    val iconColor = if (isForwarderReady) {
        Color.Green
    } else {
        Color.Red
    }

    val hint = if (isForwarderReady) {
        stringResource(R.string.settings_screen_forwarding_ready)
    } else {
        stringResource(R.string.settings_screen_forwarding_not_ready)
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            modifier = Modifier.size(90.dp),
            painter = icon,
            tint = iconColor,
            contentDescription = null, // TODO: add real description
        )
        Text(
            text = hint,
            textAlign = TextAlign.Center,
        )
    }
}