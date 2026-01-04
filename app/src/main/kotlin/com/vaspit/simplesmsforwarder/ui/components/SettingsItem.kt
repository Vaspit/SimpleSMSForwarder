package com.vaspit.simplesmsforwarder.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vaspit.simplesmsforwarder.ui.theme.SimpleSMSForwarderTheme

@Composable
fun SettingsItem(
    title: String,
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = title,
            fontWeight = FontWeight.Bold,
        )
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
        )
    }
}

@Preview
@Composable
private fun SettingItemPreview() {
    SimpleSMSForwarderTheme {
        SettingsItem(
            title = "Telegram Token",
            value = TextFieldValue(),
            onValueChange = {},
        )
    }
}