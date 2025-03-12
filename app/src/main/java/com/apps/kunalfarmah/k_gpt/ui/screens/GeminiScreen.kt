package com.apps.kunalfarmah.k_gpt.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.apps.kunalfarmah.k_gpt.ui.components.ChatBubble
import com.apps.kunalfarmah.k_gpt.ui.components.Input
import com.apps.kunalfarmah.k_gpt.ui.components.ThinkingBubble
import com.apps.kunalfarmah.k_gpt.viewmodel.GeminiViewModel

@Preview
@Composable
fun GeminiScreen(modifier: Modifier = Modifier, viewModel: GeminiViewModel = hiltViewModel()) {
    var messages = viewModel.messages.collectAsStateWithLifecycle()
    val isLoading = viewModel.isLoading.collectAsStateWithLifecycle()
    var isTyping by remember{
        mutableStateOf(false)
    }

    LaunchedEffect(messages) {
        Log.d("Messages", messages.value.toString())
    }

    Column(modifier = modifier.fillMaxSize().imePadding()) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth().weight(10f),
        ) {
            items(items = messages.value, key = {it.id}) {
                ChatBubble(
                    message = it
                )
            }
            item{
                if(isLoading.value){
                    ThinkingBubble()
                }
            }
        }
        Input(modifier = Modifier.weight(1f), onTyping = {isTyping = true}, onSubmit = {isTyping = false}, onSend = {
            viewModel.generateRequest(request = it)
        })
    }
}