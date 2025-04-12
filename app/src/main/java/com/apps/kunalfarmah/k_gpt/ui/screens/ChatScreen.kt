package com.apps.kunalfarmah.k_gpt.ui.screens

import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.apps.kunalfarmah.k_gpt.Constants
import com.apps.kunalfarmah.k_gpt.MainActivity
import com.apps.kunalfarmah.k_gpt.data.ImageData
import com.apps.kunalfarmah.k_gpt.dataStore
import com.apps.kunalfarmah.k_gpt.getMaxTokens
import com.apps.kunalfarmah.k_gpt.network.model.Event
import com.apps.kunalfarmah.k_gpt.setMaxTokens
import com.apps.kunalfarmah.k_gpt.ui.components.ChatBubble
import com.apps.kunalfarmah.k_gpt.ui.components.Input
import com.apps.kunalfarmah.k_gpt.ui.components.KeepScreenOn
import com.apps.kunalfarmah.k_gpt.ui.components.MaxTokensDialog
import com.apps.kunalfarmah.k_gpt.ui.components.ModeSwitch
import com.apps.kunalfarmah.k_gpt.ui.components.ModelSpinner
import com.apps.kunalfarmah.k_gpt.ui.components.ThinkingBubble
import com.apps.kunalfarmah.k_gpt.viewmodel.base.ChatViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ChatScreen(modifier: Modifier = Modifier, viewModel: ChatViewModel = hiltViewModel(), platform: String, textMode: Boolean, setTextMode: (Boolean) -> Unit){

    var messages = viewModel.messages.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    var messagesLoading by rememberSaveable {
        mutableStateOf(false)
    }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    var isResponding by remember{
        mutableStateOf(false)
    }

    var showDialog by remember {
        mutableStateOf(false)
    }

    var maxTokens by remember {
        mutableStateOf<Int?>(null)
    }

    var limitExceededError by remember {
        mutableStateOf("")
    }

    var isTextMode by remember {
        mutableStateOf(textMode)
    }

    var models by remember {
        mutableStateOf(platform.let{
            if(it == "Gemini"){
                if(isTextMode) {
                    Constants.geminiTextModels
                }
                else{
                    Constants.geminiImageModels
                }
            }
            else{
                if(isTextMode) {
                    Constants.openAITextModels
                }
                else{
                    Constants.openAIImageModels
                }
            }
        })
    }

    var model by rememberSaveable {
        mutableStateOf(models[0])
    }

    var previousContentSize by remember { mutableStateOf(IntSize(0,0)) }
    var currentContentSize by remember { mutableStateOf(IntSize(0,0)) }

    var chatBubbleSize by remember {
        mutableIntStateOf(0)
    }


    val context = LocalContext.current
    val datastore = context.dataStore

    LaunchedEffect(true) {
        launch {
            datastore.getMaxTokens().let {
                maxTokens = if (it == null || it == 0) {
                    null
                } else {
                    it
                }
            }
        }
        launch {
            viewModel.historyLoading.collect {
                messagesLoading = it.isLoading
            }
        }
        launch {
            viewModel.alerts.collect {
                if (it is Event.MaxTokensDialog) {
                    showDialog = it.show
                }
                if (it is Event.Toast) {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
                if (it is Event.LimitExceeded) {
                    limitExceededError = it.message
                }
            }
        }
    }

    LaunchedEffect(isResponding) {
        if(!isResponding && limitExceededError.isNotEmpty()){
            Toast.makeText(context, limitExceededError, Toast.LENGTH_SHORT).show()
            limitExceededError = ""
        }
    }

    LaunchedEffect(models) {
        model = models[0]
    }

    LaunchedEffect(textMode) {
        isTextMode = textMode
        models = if(textMode){
            if(platform == "Gemini"){
                Constants.geminiTextModels
            } else{
                Constants.openAITextModels
            }
        } else {
            if (platform == "Gemini") {
                Constants.geminiImageModels
            } else {
                Constants.openAIImageModels
            }
        }
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

    LaunchedEffect(messages.value.size, isImeVisible, isResponding, currentContentSize) {
        if(messages.value.isNotEmpty() && (!isResponding || (isImeVisible || currentContentSize.height > previousContentSize.height))){
            listState.scrollToItem(messages.value.size - 1, chatBubbleSize)
            previousContentSize = currentContentSize
        }
        else if(messages.value.isNotEmpty()){
            listState.animateScrollToItem(messages.value.size - 1)
        }
    }

    if(isResponding || isLoading || messagesLoading){
        KeepScreenOn()
    }

    if(messagesLoading){
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(modifier = Modifier.size(40.dp))
            Text(text = "Loading $platform message history", modifier = Modifier.padding(20.dp), fontSize = 15.sp, color = MaterialTheme.colorScheme.onSurface)
        }
    } else {
        Column(
            modifier = modifier.imePadding()
        ) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
                ModeSwitch(textMode = textMode, onToggle = {
                    viewModel.clearImageData()
                    setTextMode(it)
                })
                Text(modifier = Modifier.padding(start = 10.dp, end=10.dp), text = "|", fontSize = 20.sp)
                ModelSpinner(type = platform, models = models, onModelSelected = { model = it })
            }
            LazyColumn(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .weight(1f)
                    .onSizeChanged {
                        currentContentSize = it
                    },
                state = listState
            ) {

                items(items = messages.value, key = { it.id }) {
                    ChatBubble(
                        modifier = Modifier.onGloballyPositioned { pos ->
                            if (messages.value.last().id === it.id) {
                                chatBubbleSize = pos.size.height
                            }
                        },
                        message = it,
                        listState = listState,
                        isResponding = isResponding,
                        isThinking = false,
                        onResponseCompleted = {
                            isResponding = false
                        },
                        onImageSelected = { imageData ->
                            viewModel.setDownloadedImageData(ImageData(bitmap = imageData.bitmap, mimeType = imageData.mimeType, platform = platform))
                        }
                    )
                }
                item {
                    if (isLoading) {
                        ThinkingBubble()
                    }
                }
            }
            Input(
                onSend = { text ->
                    if (text.isNotBlank()) {
                        isResponding = true
                        model.let { model ->
                            if (!isTextMode) {
                                viewModel.generateImage(model = model, request = text)
                            } else {
                                viewModel.generateResponse(
                                    model = model,
                                    request = text,
                                    maxTokens = maxTokens
                                )
                            }
                        }
                    }
                },
                onAttachImage = {
                  viewModel.clearImageData()
                    (context as MainActivity).pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
                },
                placeHolder =
                    model.let {
                        if (!isTextMode) {
                            "Enter your image prompt"
                        } else {
                            "Enter your message"
                        }

                    },
                isResponding = !isLoading && isResponding,
                isThinking = isLoading,
                onResponseStopped = {
                    isResponding = false
                    try {
                        scope.launch {
                            delay(100)
                            listState.scrollToItem(messages.value.size - 1, chatBubbleSize)
                        }
                    } catch (_: Exception) {}
                })
        }
    }

    MaxTokensDialog(
        showDialog = showDialog,
        onDismiss = { viewModel.toggleMaxTokensDialog(false) },
        maxTokens = maxTokens,
        onSave = {
            maxTokens = it
            scope.launch {
                datastore.setMaxTokens(it ?: 0)
            }
            viewModel.toggleMaxTokensDialog(false)
        }
    )

}
