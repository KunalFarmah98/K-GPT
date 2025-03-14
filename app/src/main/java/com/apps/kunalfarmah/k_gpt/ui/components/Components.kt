package com.apps.kunalfarmah.k_gpt.ui.components

import android.util.Log
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.apps.kunalfarmah.k_gpt.Constants
import com.apps.kunalfarmah.k_gpt.data.Message
import com.apps.kunalfarmah.k_gpt.ui.screens.bottomTabs
import com.apps.kunalfarmah.k_gpt.util.Util.getDate
import kotlin.math.roundToInt

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
                .size(55.dp)
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
            Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.primary), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
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
            Text(modifier = Modifier.padding(10.dp), textAlign = TextAlign.Start, text = message.text, color = MaterialTheme.colorScheme.onPrimary)
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


@Preview
@Composable
fun ModelSpinner(modifier: Modifier = Modifier, onModelSelected: (Constants.GeminiModels) -> Unit = {}) {
    var expanded by rememberSaveable {
        mutableStateOf(false)
    }
    var selectedModel by rememberSaveable {
        mutableStateOf(Constants.GeminiModels.GEMINI_2_0_FLASH)
    }
    val modelsList = listOf(
        Constants.GeminiModels.GEMINI_2_0_FLASH,
        Constants.GeminiModels.GEMINI_2_0_FLASH_LITE,
        Constants.GeminiModels.GEMINI_1_5_FLASH,
        Constants.GeminiModels.GEMINI_1_5_PRO
    )
    var parentWidth by remember {
        mutableStateOf(0.dp)
    }

    Row (modifier = modifier
        .fillMaxWidth()
        .padding(top = 10.dp, end = 10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End){
        Text(text = "AI Model:  ", color = MaterialTheme.colorScheme.onSurface)
        Box(modifier = Modifier
            .onGloballyPositioned {
                parentWidth = it.size.toSize().width.dp
            }){
            Text(
                text = selectedModel.modelName,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .clip(RectangleShape)
                    .border(width = 1.dp, color = MaterialTheme.colorScheme.primary)
                    .padding(2.dp)
                    .width(175.dp)
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
                        text = { Text(item.modelName) },
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

@Preview
@Composable
fun BottomTabBar(modifier: Modifier = Modifier, navController: NavHostController = rememberNavController()){
    BottomNavigation(
        windowInsets = WindowInsets.navigationBars,
        backgroundColor = MaterialTheme.colorScheme.primary
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        bottomTabs.forEach { item ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        item.icon,
                        contentDescription = item.name
                    )
                },
                label = { Text(item.name) },
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
                unselectedContentColor = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}