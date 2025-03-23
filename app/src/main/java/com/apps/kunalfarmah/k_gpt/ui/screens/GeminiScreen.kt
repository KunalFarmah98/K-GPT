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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.apps.kunalfarmah.k_gpt.GeminiModels
import com.apps.kunalfarmah.k_gpt.dataStore
import com.apps.kunalfarmah.k_gpt.getMaxTokens
import com.apps.kunalfarmah.k_gpt.network.model.Event
import com.apps.kunalfarmah.k_gpt.setMaxTokens
import com.apps.kunalfarmah.k_gpt.ui.components.ChatBubble
import com.apps.kunalfarmah.k_gpt.ui.components.Input
import com.apps.kunalfarmah.k_gpt.ui.components.MaxTokensDialog
import com.apps.kunalfarmah.k_gpt.ui.components.ModelSpinner
import com.apps.kunalfarmah.k_gpt.ui.components.ThinkingBubble
import com.apps.kunalfarmah.k_gpt.viewmodel.GeminiViewModel
import kotlinx.coroutines.launch

@Preview
@Composable
fun GeminiScreen(modifier: Modifier = Modifier, viewModel: GeminiViewModel = hiltViewModel()) {
    var messages = viewModel.messages.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    var model by rememberSaveable {
        mutableStateOf(GeminiModels.GEMINI_2_0_FLASH.modelName)
    }
    var isResponding by remember{
        mutableStateOf(false)
    }

    var showDialog by remember {
        mutableStateOf(false)
    }

    var maxTokens by remember {
        mutableIntStateOf(Int.MAX_VALUE)
    }


    val context = LocalContext.current
    val datastore = context.dataStore

    LaunchedEffect(true) {
        viewModel.alerts.collect {
            if(it is Event.MaxTokensDialog){
                showDialog = it.show
            }
        }
        maxTokens = datastore.getMaxTokens()
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
        ModelSpinner(type = "Gemini", onModelSelected = {model = it})
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

    MaxTokensDialog(
        showDialog = showDialog,
        onDismiss = { viewModel.toggleMaxTokensDialog(false) },
        maxTokens = maxTokens,
        onSave = {
            maxTokens = it
            scope.launch {
                datastore.setMaxTokens(it)
            }
        }
    )
}