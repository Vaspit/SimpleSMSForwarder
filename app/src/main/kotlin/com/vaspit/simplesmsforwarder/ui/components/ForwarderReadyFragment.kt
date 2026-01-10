package com.vaspit.simplesmsforwarder.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.vaspit.simplesmsforwarder.R

@Composable
fun ForwarderReadyFragment(
    modifier: Modifier = Modifier,
    isForwarderReady: Boolean,
) {
    val icon = if (isForwarderReady) {
        painterResource(R.drawable.ic_forwarding_ready)
    } else {
        painterResource(R.drawable.ic_forwarding_not_ready)
    }

    val color = if (isForwarderReady) {
        Color.Green
    } else {
        Color.Red
    }

    Icon(
        modifier = modifier.size(90.dp),
        painter = icon,
        tint = color,
        contentDescription = null, // TODO: add real description
    )
}