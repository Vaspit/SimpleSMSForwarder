package com.vaspit.simplesmsforwarder.ui.components

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.vaspit.simplesmsforwarder.core.presentation.ButtonState

@Composable
fun SMSForwarderButton(
    modifier: Modifier = Modifier,
    buttonState: ButtonState,
    onClick: () -> Unit,
) {
    val context = LocalContext.current
    Button(
        modifier = modifier,
        enabled = buttonState.isEnabled,
        content = {
            Text(text = buttonState.text.asString(context))
        },
        onClick = onClick,
    )
}