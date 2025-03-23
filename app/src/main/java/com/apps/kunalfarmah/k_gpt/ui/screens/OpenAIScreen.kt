package com.apps.kunalfarmah.k_gpt.ui.screens

import android.view.ViewTreeObserver
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.apps.kunalfarmah.k_gpt.OpenAIModels
import com.apps.kunalfarmah.k_gpt.ui.components.ChatBubble
import com.apps.kunalfarmah.k_gpt.ui.components.Input
import com.apps.kunalfarmah.k_gpt.ui.components.ModelSpinner
import com.apps.kunalfarmah.k_gpt.ui.components.ThinkingBubble
import com.apps.kunalfarmah.k_gpt.viewmodel.OpenAIViewModel

@Preview
@Composable
fun OpenAIScreen(modifier: Modifier = Modifier, viewModel: OpenAIViewModel = hiltViewModel()) {
    var messages = viewModel.messages.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    var model by rememberSaveable {
        mutableStateOf(OpenAIModels.GPT_4O_MINI.modelName)
    }

    var isResponding by remember{
        mutableStateOf(false)
    }

    // Access the current View and keyboard visibility state
    val view = LocalView.current
    var isImeVisible by remember { mutableStateOf(false) }

    // Observe keyboard visibility using ViewTreeObserver
    DisposableEffect(Unit) {
        val listener = ViewTreeObserver.OnPreDrawListener {
            // Check if the IME (keyboard) is visible
            isImeVisible = ViewCompat.getRootWindowInsets(view)
                ?.isVisible(WindowInsetsCompat.Type.ime()) == true
            true
        }
        view.viewTreeObserver.addOnPreDrawListener(listener)
        onDispose {
            view.viewTreeObserver.removeOnPreDrawListener(listener)
        }
    }

    LaunchedEffect(messages.value.size, isImeVisible, isResponding) {
        if(isImeVisible && messages.value.isNotEmpty()){
            listState.scrollToItem(messages.value.size - 1)
        }
        else if(messages.value.isNotEmpty()){
            listState.animateScrollToItem(messages.value.size - 1)
        }
    }

    Column(
        modifier = modifier.imePadding()
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
                    message = it,
                    listState = listState,
                    isResponding = isResponding,
                    onResponseCompleted = {
                        isResponding = false
                    }
                )
            }
            item{
                if(isLoading){
                    ThinkingBubble()
                }
            }
        }
        Input(
            onSend = {
                if (it.isNotBlank()) {
                    viewModel.generateRequest(model = model, request = it)
                    isResponding = true
                }
            },
            isResponding = !isLoading && isResponding,
            onResponseStopped = {
                isResponding = false
            })
    }
}