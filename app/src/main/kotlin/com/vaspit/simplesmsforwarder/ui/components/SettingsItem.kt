package com.vaspit.simplesmsforwarder.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vaspit.simplesmsforwarder.R
import com.vaspit.simplesmsforwarder.ui.theme.SimpleSMSForwarderTheme

@Composable
fun SettingsItem(
    title: String,
    value: TextFieldValue,
    placeholder: String,
    focusRequester: FocusRequester,
    onValueChange: (TextFieldValue) -> Unit,
    onClearTextClick: () -> Unit,
    imeAction: ImeAction,
    onImeAction: () -> Unit,
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
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            value = value,
            placeholder = {
                Text(text = placeholder)
            },
            trailingIcon = {
                if (value.text.isNotEmpty()) {
                    Icon(
                        modifier = Modifier.clickable {
                            onClearTextClick()
                        },
                        painter = painterResource(R.drawable.ic_cancel),
                        contentDescription = stringResource(R.string.clear_text_field_button),
                    )
                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = imeAction,
            ),
            keyboardActions = KeyboardActions(
                onNext = { if (imeAction == ImeAction.Next) onImeAction() },
                onDone = { if (imeAction == ImeAction.Done) onImeAction() },
            ),
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
            placeholder = "Enter telegram token here",
            focusRequester = remember { FocusRequester() },
            onValueChange = {},
            onClearTextClick = {},
            imeAction = ImeAction.Next,
            onImeAction = {},
        )
    }
}