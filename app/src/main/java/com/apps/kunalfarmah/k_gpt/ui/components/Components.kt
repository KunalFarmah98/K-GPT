package com.apps.kunalfarmah.k_gpt.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.apps.kunalfarmah.k_gpt.data.Message
import com.apps.kunalfarmah.k_gpt.util.Util.getDate

@Preview
@Composable
fun Input(modifier: Modifier = Modifier, onSend: (String) -> Unit = {}, onTyping: () -> Unit = {}, onSubmit: () -> Unit = {}){
    var text by rememberSaveable {
        mutableStateOf("")
    }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    ConstraintLayout(modifier= modifier
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
            placeholder = { Text("Enter your message") },
            value = text,
            shape = RoundedCornerShape(16.dp),
            onValueChange = {
                onTyping()
                text = it
            },
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent, // Hide the indicator line when focused
                unfocusedIndicatorColor = Color.Transparent, // Hide the indicator line when not focused
                disabledIndicatorColor = Color.Transparent // Hide the indicator line when disabled
            )
        )
        Card(
            modifier
                .constrainAs(sendButton) {
                    end.linkTo(parent.end)
                    bottom.linkTo(textField.bottom)
                }
                .padding(end = 8.dp, bottom = 8.dp)
                .size(50.dp)
                .clickable {
                    onSend(text)
                    text = ""
                    focusManager.clearFocus()
                    keyboardController?.hide()
                    onSubmit()
                },
            shape = CircleShape,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "send",
                    modifier = Modifier
                        .size(25.dp)
                )
            }

        }

    }
}

@Preview
@Composable
fun ChatBubble(modifier: Modifier = Modifier, message: Message = Message(text = "Hello"), isThinking: Boolean = false){
    val isUser = message.isUser
    val bubbleShape = RoundedCornerShape(
        topStart = 16.dp,
        topEnd = 16.dp,
        bottomStart = if (isUser) 16.dp else 0.dp,
        bottomEnd = if (isUser) 0.dp else 16.dp
    )
    val backgroundColor = if (isUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
    val screenWidth = LocalConfiguration.current.screenWidthDp

    var boxModifier = modifier
        .clip(bubbleShape)
        .background(backgroundColor)
    if(isThinking){
        boxModifier  = boxModifier.width(100.dp)
    }
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = if (isUser) Alignment.End else Alignment.Start) {
        Box(
            modifier = boxModifier.widthIn(min = 50.dp, max = (screenWidth*0.8f).toInt().dp)
        ) {
            Text(modifier = Modifier.padding(10.dp), textAlign = TextAlign.Start, text = message.text)
        }
        if(!isThinking){
            Text(modifier = Modifier.padding(top = 2.dp, end = if(isUser) 10.dp else 0.dp, start = if(isUser) 0.dp else 10.dp), textAlign = TextAlign.Center, text = getDate(message.time), fontSize = 10.sp)
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

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun AppBar(title: String = "Gemini") {
    TopAppBar(
        title = {
            Text(title)
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}