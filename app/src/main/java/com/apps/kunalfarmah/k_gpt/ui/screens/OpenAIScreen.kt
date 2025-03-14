package com.apps.kunalfarmah.k_gpt.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.apps.kunalfarmah.k_gpt.OpenAIModels
import com.apps.kunalfarmah.k_gpt.ui.components.ChatBubble
import com.apps.kunalfarmah.k_gpt.ui.components.Input
import com.apps.kunalfarmah.k_gpt.ui.components.ModelSpinner
import com.apps.kunalfarmah.k_gpt.ui.components.ThinkingBubble
import com.apps.kunalfarmah.k_gpt.viewmodel.OpenAIViewModel
import kotlinx.coroutines.launch

@Preview
@Composable
fun OpenAIScreen(modifier: Modifier = Modifier, viewModel: OpenAIViewModel = hiltViewModel()) {
    var messages = viewModel.messages.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    var model by rememberSaveable {
        mutableStateOf(OpenAIModels.GPT_4O_MINI.modelName)
    }
    var isTyping by remember{
        mutableStateOf(false)
    }

    LaunchedEffect(messages.value.size) {
        if(messages.value.isNotEmpty()){
            coroutineScope.launch {
                listState.animateScrollToItem(messages.value.size - 1)
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
    ) {
        ModelSpinner(type = "OpenAI", onModelSelected = {model = it})
        LazyColumn(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .weight(1f),
            state = listState
        ) {

            items(items = messages.value, key = {it.id}) {
                ChatBubble(
                    message = it
                )
            }
            item{
                if(isLoading){
                    ThinkingBubble()
                }
            }
        }
        Input(onTyping = { isTyping = true }, onSubmit = { isTyping = false }, onSend = {
            if (it.isNotBlank()) {
                viewModel.generateRequest(model = model, request = it)
            }
        })
    }
}