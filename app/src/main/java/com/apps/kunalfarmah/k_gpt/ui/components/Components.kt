package com.apps.kunalfarmah.k_gpt.ui.components

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.widget.Toast
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.ripple
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.apps.kunalfarmah.k_gpt.MainActivity
import com.apps.kunalfarmah.k_gpt.R
import com.apps.kunalfarmah.k_gpt.data.ImageData
import com.apps.kunalfarmah.k_gpt.data.Message
import com.apps.kunalfarmah.k_gpt.ui.screens.Screens
import com.apps.kunalfarmah.k_gpt.ui.screens.bottomTabs
import com.apps.kunalfarmah.k_gpt.util.Util
import com.apps.kunalfarmah.k_gpt.util.Util.animatedMessages
import com.apps.kunalfarmah.k_gpt.util.Util.getDate
import com.apps.kunalfarmah.k_gpt.util.Util.getTime
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun KeepScreenOn() {
    val currentView = LocalView.current
    val context = currentView.context
    DisposableEffect(Unit) {
        val window = (context as? Activity)?.window
            ?: return@DisposableEffect onDispose {}
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        onDispose {
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }
}

@Preview
@Composable
fun Input(modifier: Modifier = Modifier, placeHolder: String = "", onSend: (String) -> Unit = {}, isThinking: Boolean = false, isResponding: Boolean = false, onResponseStopped: () -> Unit = {}, onAttachImage: () -> Unit = {}){
    var text by rememberSaveable {
        mutableStateOf("")
    }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    ConstraintLayout(modifier= modifier
        .padding(bottom = 2.dp)
        .fillMaxWidth()
        ) {
        val (textField, sendButton) = createRefs()
        TextField(
            modifier = Modifier
                .constrainAs(textField) {
                    start.linkTo(parent.start)
                    end.linkTo(sendButton.start)
                    top.linkTo(parent.top)
                    width = Dimension.fillToConstraints
                }
                .padding(8.dp),
            placeholder = { Text(placeHolder) },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Send,
                autoCorrectEnabled = false,
                capitalization = KeyboardCapitalization.None
            ),
            keyboardActions = KeyboardActions(
                onSend = {
                    onSend(text)
                    text = ""
                    focusManager.clearFocus()
                    keyboardController?.hide()
                }
            ),
            value = text,
            shape = RoundedCornerShape(16.dp),
            onValueChange = {
                text = it
            },
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent, // Hide the indicator line when focused
                unfocusedIndicatorColor = Color.Transparent, // Hide the indicator line when not focused
                disabledIndicatorColor = Color.Transparent // Hide the indicator line when disabled
            ),
            trailingIcon = {
                IconButton(onClick = onAttachImage) {
                    Icon(
                        modifier = Modifier.padding(end = 0.dp),
                        painter = painterResource(R.drawable.baseline_attach_file_24),
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = "image"
                    )
                }
            }
        )
        Card(
            modifier
                .constrainAs(sendButton) {
                    end.linkTo(parent.end)
                    bottom.linkTo(textField.bottom)
                }
                .padding(end = 8.dp, bottom = 8.dp)
                .size(55.dp)
                .alpha(if (isThinking) 0.5f else 1f)
                .clickable {
                    if (isThinking) {
                        return@clickable
                    }
                    if (isResponding) {
                        onResponseStopped()
                    } else {
                        onSend(text)
                        text = ""
                        focusManager.clearFocus()
                        keyboardController?.hide()
                    }
                },
            shape = CircleShape,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Column(modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if(isResponding){
                    Image(
                        painter = painterResource(id = R.drawable.baseline_stop_24),
                        contentDescription = "stop",
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
                    )
                }
                else {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "send",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .size(25.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun StyledText(text: String, color: Color = Color.Unspecified) {
    val annotatedString = buildAnnotatedString {
        var startIndex = 0
        while (startIndex < text.length) {
            val boldStart = text.indexOf("**", startIndex)
            if (boldStart == -1) {
                append(text.substring(startIndex))
                break
            }
            val boldEnd = text.indexOf("**", boldStart + 2)
            if (boldEnd == -1) {
                append(text.substring(startIndex))
                break
            }
            append(text.substring(startIndex, boldStart))
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append(text.substring(boldStart + 2, boldEnd))
            }
            startIndex = boldEnd + 2
        }
    }
    Text(text = annotatedString, modifier = Modifier.padding(10.dp), textAlign = TextAlign.Start, color = color)
}


@Composable
fun AnimatedStyledText(text: String, color: Color = Color.Unspecified, animateText: Boolean = true, onAnimated: () -> Unit = {},
                       onHeightChanged: (Int) -> Unit = {}, isResponding: Boolean) {
    val words = text.split(" ")
    val animatedWords = remember { mutableStateListOf<String>() }
    var showAnimation by remember {
        mutableStateOf(isResponding)
    }
    // Update showAnimation whenever isResponding changes
    LaunchedEffect(isResponding) {
        showAnimation = isResponding
    }

    LaunchedEffect(showAnimation) {
        if(!showAnimation){
            onAnimated()
        }
        if(animateText && showAnimation) {
            for (word in words) {
                if (!showAnimation) {
                    break
                }
                animatedWords.add(word)
                delay(100) // Delay between each word
            }
            onAnimated()
        }
        else{
            animatedWords.clear()
            animatedWords.addAll(words)
        }
    }
    val annotatedString = buildAnnotatedString {
        var startIndex = 0
        val annotatedText = animatedWords.joinToString(" ")
        while (startIndex < annotatedText.length) {
            val boldStart = annotatedText.indexOf("**", startIndex)
            if (boldStart == -1) {
                append(annotatedText.substring(startIndex))
                break
            }
            val boldEnd = annotatedText.indexOf("**", boldStart + 2)
            if (boldEnd == -1) {
                append(annotatedText.substring(startIndex))
                break
            }
            append(annotatedText.substring(startIndex, boldStart))
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append(annotatedText.substring(boldStart + 2, boldEnd))
            }
            startIndex = boldEnd + 2
        }
    }
    Text(text = annotatedString, modifier = Modifier
        .padding(10.dp)
        .onSizeChanged {
            onHeightChanged(it.height)
        }, textAlign = TextAlign.Start, color = color)
}

@Composable
fun DisplayImageWithDownload(imageData: String, mimeType: String, isUserMessage: Boolean = false, onDownloadClick: (bitmap: Bitmap) -> Unit = {}) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        val bitmap = Util.decodeImage(imageData, mimeType)
        if (bitmap != null) {
            Box {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Decoded Image",
                    contentScale = ContentScale.Fit
                )
                if(!isUserMessage) {
                    Card(
                        onClick = { onDownloadClick(bitmap) },
                        colors = CardDefaults.cardColors()
                            .copy(containerColor = MaterialTheme.colorScheme.primary),
                        modifier = Modifier
                            .size(45.dp)
                            .align(Alignment.BottomEnd)
                            .padding(end = 8.dp, bottom = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                modifier = Modifier.size(20.dp),
                                painter = painterResource(R.drawable.baseline_download_24),
                                contentDescription = "Download Image",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
            }
        } else {
            // Handle the case where decoding fails (e.g., display an error message)
            // For now, we'll just leave it empty.
        }
    }
}

@Preview
@Composable
fun ModeSwitch(modifier: Modifier = Modifier, textMode: Boolean = true, onToggle: (Boolean) -> Unit = {}){
    var isToggled by rememberSaveable {
        mutableStateOf(false)
    }
    LaunchedEffect(textMode) {
        isToggled = !textMode
    }
    Row(modifier = modifier.padding(start = 10.dp), verticalAlignment = Alignment.CenterVertically){
        Text(text = "Text", color = MaterialTheme.colorScheme.onSurface, fontSize = 15.sp)
        Switch(checked = isToggled, onCheckedChange = {
            onToggle(!textMode)
        }, colors = SwitchDefaults.colors(checkedTrackColor = MaterialTheme.colorScheme.secondary, uncheckedTrackColor = MaterialTheme.colorScheme.secondary, checkedThumbColor = MaterialTheme.colorScheme.primary, uncheckedThumbColor = MaterialTheme.colorScheme.primary))
        Text(text = "Image", color = MaterialTheme.colorScheme.onSurface, fontSize = 15.sp)
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun ChatBubble(modifier: Modifier = Modifier, message: Message = Message(text = "Hello"), isThinking: Boolean = false, listState: LazyListState = rememberLazyListState(), isResponding: Boolean=false, onResponseCompleted: () -> Unit = {}, onImageSelected: (ImageData) -> Unit = {}){
    val isUser = message.isUser
    val coroutineScope = rememberCoroutineScope()
    val bubbleShape = RoundedCornerShape(
        topStart = 16.dp,
        topEnd = 16.dp,
        bottomStart = if (isUser) 16.dp else 0.dp,
        bottomEnd = if (isUser) 0.dp else 16.dp
    )
    val backgroundColor = if (isUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
    val screenWidth = LocalConfiguration.current.screenWidthDp

    var animateText by rememberSaveable {
        mutableStateOf(!animatedMessages.contains(message.id))
    }

    val interactionSource = remember { MutableInteractionSource() }
    val context = LocalContext.current

    var boxModifier = modifier
        .clip(bubbleShape)
        .background(backgroundColor)
        .widthIn(min = 50.dp, max = (screenWidth * 0.8f).toInt().dp)

    val haptic = LocalHapticFeedback.current
    var onDownloadClick: (bitmap: Bitmap) -> Unit = {}
    var fileName = ""
    boxModifier = if (isThinking) {
        boxModifier.width(100.dp)
    }
    else if (message.isImage) {
        if (message.isUser) {
            boxModifier.sizeIn(maxWidth = 200.dp, maxHeight = 200.dp)
        } else {
            fileName = "K-GPT_Image_${Util.getImageTime(message.time)}".plus(
                when (message.mimeType) {
                    "image/jpeg" -> ".jpg"
                    "image/png" -> ".png"
                    else -> ".jpg"
                }
            )
            onDownloadClick = { bitmap ->
                onImageSelected(ImageData(bitmap = bitmap, mimeType = message.mimeType))
                val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)
                    type = message.mimeType
                    putExtra(Intent.EXTRA_TITLE, fileName)
                }
                (context as MainActivity).saveImageLauncher.launch(intent)
            }
            boxModifier
                .combinedClickable(
                    interactionSource = interactionSource,
                    indication = ripple(bounded = true),
                    onClick = {},
                    onLongClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onDownloadClick(
                            Util.decodeImage(
                                message.imageData!!,
                                message.mimeType ?: "image/png"
                            )!!
                        )
                    }
                )
        }
    }
    else{
        boxModifier.combinedClickable(
            interactionSource = interactionSource,
            indication = ripple(bounded = true),
            onClick = {},
            onLongClick = {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                Util.copyToClipboard(context, message.text)
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
                    Toast.makeText(
                        context,
                        context.getString(R.string.copied_message_to_clipboard),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )
    }


    var lastHeight by remember { mutableIntStateOf(0) }

    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = if (isUser) Alignment.End else Alignment.Start) {
        if(message.firstMessageInDay){
            Text(modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp), color = MaterialTheme.colorScheme.onSurface, text = getDate(message.time), fontSize = 12.sp, textAlign = TextAlign.Center)
        }
        Box(
            modifier = boxModifier
        ) {
            // do not animate user messages or messages from history
            if(message.isImage){
                DisplayImageWithDownload(imageData = message.imageData!!, mimeType = message.mimeType!!, isUserMessage = message.isUser, onDownloadClick = onDownloadClick)
                onResponseCompleted()
            }
            else if (message.isUser || isThinking) {
                Text(text = message.text, modifier = Modifier.padding(10.dp), textAlign = TextAlign.Start, color = MaterialTheme.colorScheme.onPrimary)
            }
            else if(message.fromHistory || !animateText){
                StyledText(text = message.text, color = MaterialTheme.colorScheme.onPrimary)
            }
            else {
                AnimatedStyledText(
                    text = message.text,
                    color = MaterialTheme.colorScheme.onPrimary,
                    animateText = animateText,
                    isResponding = isResponding,
                    onAnimated = {
                        animatedMessages.add(message.id)
                        animateText = false
                        onResponseCompleted()
                    },
                    onHeightChanged = { newHeight ->
                        if (newHeight > lastHeight) {
                            Log.d("TAG", "ChatBubble: $newHeight - $lastHeight")
                            val offset = newHeight - lastHeight
                            val index =
                                listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                            lastHeight = newHeight
                            if (offset > 20) {
                                if (index >= listState.firstVisibleItemIndex) {
                                    Log.d("TAG", "ChatBubble: $offset")
                                    val scrollBy = offset
                                    val current = listState.firstVisibleItemScrollOffset
                                    coroutineScope.launch {
                                        listState.scrollToItem(index, current - scrollBy)
                                    }
                                }
                            }
                        }
                    }
                )
            }
        }
        if(!isThinking){
            Text(modifier = Modifier.padding(top = 2.dp, end = if(isUser) 10.dp else 0.dp, start = if(isUser) 0.dp else 10.dp), textAlign = TextAlign.Center, text = getTime(message.time), fontSize = 10.sp)
        }
    }
}

@Preview
@Composable
fun ThinkingBubble(modifier: Modifier = Modifier) {
    val maxDots = 3f
    val minDots = 1f
    val animationDuration = 500

    // Create an infinite transition
    val infiniteTransition = rememberInfiniteTransition(label = "InfiniteTransition")

    // Animate the dot count
    val dots by infiniteTransition.animateFloat(
        initialValue = minDots,
        targetValue = maxDots,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = animationDuration),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dots",
    )
    val text = "Thinking" + ".".repeat(dots.toInt())
    ChatBubble(message = Message(text = text, isUser = false), modifier = modifier, isThinking = true)
}


@Preview
@Composable
fun ModelSpinner(modifier: Modifier = Modifier, models:  List<String> = listOf<String>(), type: String = "Gemini", onModelSelected: (String) -> Unit = {}) {
    var expanded by rememberSaveable {
        mutableStateOf(false)
    }
    var modelsList by rememberSaveable {
        mutableStateOf(models)
    }

    var selectedModel by rememberSaveable {
        mutableStateOf(modelsList[0])
    }

    var parentWidth by remember {
        mutableStateOf(0.dp)
    }

    LaunchedEffect(models) {
        modelsList = models
        selectedModel = modelsList[0]
    }

    Row (modifier = modifier
        .fillMaxWidth()
        .padding(end = 10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End){
        Text(text = stringResource(R.string.ai_model), color = MaterialTheme.colorScheme.onSurface, fontSize = 15.sp)
        Box(modifier = Modifier
            .onGloballyPositioned {
                parentWidth = it.size.toSize().width.dp
            }){
            Text(
                text = selectedModel,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Clip,
                modifier = Modifier
                    .clip(RectangleShape)
                    .border(width = 1.dp, color = MaterialTheme.colorScheme.primary)
                    .padding(2.dp)
                    .width(200.dp)
                    .clickable {
                        expanded = true
                    }
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                },
                offset = DpOffset(parentWidth.minus(10.dp).value.roundToInt().dp, 0.dp)

            ) {
                modelsList.forEachIndexed { index, item ->
                    DropdownMenuItem(
                        text = { Text(text = item, fontSize = 15.sp) },
                        onClick = {
                            expanded = false
                            selectedModel = item
                            onModelSelected(item)
                        }
                    )
                }
            }
        }

        Icon(
            modifier = modifier.clickable {
                expanded = true
            }, imageVector = if(!expanded) Icons.Filled.KeyboardArrowDown else Icons.Filled.KeyboardArrowUp, contentDescription = "select model")
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun AppBar(title: String = "Gemini", backStackEntry: NavBackStackEntry? = rememberNavController().currentBackStackEntry, onClear: (String?) -> Unit = {}, onHistory: () -> Unit = {}, onTokenSettings: () -> Unit = {}) {
    var expanded by rememberSaveable {
        mutableStateOf(false)
    }
    TopAppBar(
        title = {
            Text(title)
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        actions = {
            IconButton(onClick = onHistory) {
                Image(
                    painter = painterResource(id = R.drawable.baseline_history_24),
                    contentDescription = "history",
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
                )
            }
            IconButton(onClick = { expanded = true }) {
                Icon(modifier = Modifier.size (24.dp), tint = MaterialTheme.colorScheme.onPrimary, imageVector = Icons.Outlined.Settings, contentDescription = "settings")
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                }
            ) {
                DropdownMenuItem(
                    text = { Text("Clear Gemini History") },
                    onClick = {
                        expanded = false
                        onClear("Gemini")
                    }
                )

                DropdownMenuItem(
                    text = { Text("Clear OpenAI History") },
                    onClick = {
                        expanded = false
                        onClear("OpenAI")
                    }
                )
                DropdownMenuItem(
                    text = { Text("Clear All Message History") },
                    onClick = {
                        expanded = false
                        onClear(null)
                    }
                )
                if(backStackEntry?.destination?.hasRoute<Screens.OpenAIChatScreen>() == true) {
                    DropdownMenuItem(
                        text = { Text("Set Max Response Tokens") },
                        onClick = {
                            expanded = false
                            onTokenSettings()
                        }
                    )
                }
            }
        }
    )
}

@Preview
@Composable
fun BottomTabBar(modifier: Modifier = Modifier, navController: NavHostController = rememberNavController()){
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
    // hide bottom tabs when keyboard is open
    if(isImeVisible) {
        return
    }
    BottomAppBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary
    ){
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        bottomTabs.forEach { item ->
            BottomNavigationItem(
                icon = {
                    androidx.compose.material.Icon(
                        painterResource(item.iconId),
                        contentDescription = item.name
                    )
                },
                label = { androidx.compose.material.Text(item.name) },
                selected = currentDestination?.hierarchy?.any {
                    it.hasRoute(
                        item.route::class
                    )
                } == true,
                onClick = {
                    navController.navigate(item.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // re-selecting the same item
                        launchSingleTop = true
                        // Restore state when re-selecting a previously selected item
                        restoreState = true
                    }
                },
                selectedContentColor = MaterialTheme.colorScheme.onPrimary,
                unselectedContentColor = Color.Gray,
            )
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun MaxTokensDialog(
    showDialog: Boolean = true,
    onDismiss: () -> Unit = {},
    maxTokens: Int ?= null,
    onSave: (Int?) -> Unit = {}
) {
    if (showDialog) {
        var maxTokens by remember {
            maxTokens.let {
                if (it == null || it == 0) {
                    mutableStateOf("")
                } else {
                    mutableStateOf(it.toString())
                }
            }
        }
        Dialog(onDismissRequest = onDismiss) {
            Card(
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Set Max Response Tokens",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Text(
                        text = "(leave blank for no limit)",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = maxTokens,
                        onValueChange = { newValue ->
                            maxTokens = newValue.filter { it.isDigit() }
                        },
                        label = { Text("Max Tokens") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = onDismiss) {
                            Text("Cancel")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = {
                            maxTokens.let {
                                if (it.isEmpty()) {
                                    onSave(null)
                                } else {
                                    onSave(it.toInt())
                                }
                            }
                        }) {
                            Text("Save")
                        }
                    }
                }
            }
        }
    }
}

