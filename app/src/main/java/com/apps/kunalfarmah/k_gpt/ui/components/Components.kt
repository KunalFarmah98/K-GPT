package com.apps.kunalfarmah.k_gpt.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun Input(modifier: Modifier = Modifier, onClick: (String) -> Unit = {}){
    var text = rememberSaveable {
        mutableStateOf("")
    }
    Row(modifier= modifier
        .fillMaxWidth()
        .background(Color.White), horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically) {
        TextField(
            modifier = Modifier.weight(8f),
            placeholder = {Text("Enter your message")},
            value = text.value,
            onValueChange = { text.value = it },
        )
        Box(
            modifier = Modifier
                .weight(2f)
                .clickable { onClick(text.value) },
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = "send")
        }

    }
}