package com.apps.kunalfarmah.k_gpt.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.BottomAppBar
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
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
import com.apps.kunalfarmah.k_gpt.R
import com.apps.kunalfarmah.k_gpt.data.Message
import com.apps.kunalfarmah.k_gpt.ui.screens.bottomTabs
import com.apps.kunalfarmah.k_gpt.util.Util.getDate
import com.apps.kunalfarmah.k_gpt.util.Util.getTime
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
            placeholder = { Text("Enter your message") },
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
            Column(modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
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
        if(message.firstMessageInDay){
            Text(modifier = Modifier.fillMaxWidth().padding(5.dp), color = MaterialTheme.colorScheme.onSurface, text = getDate(message.time), fontSize = 12.sp, textAlign = TextAlign.Center)
        }
        Box(
            modifier = boxModifier.widthIn(min = 50.dp, max = (screenWidth*0.8f).toInt().dp)
        ) {
            Text(modifier = Modifier.padding(10.dp), textAlign = TextAlign.Start, text = message.text, color = MaterialTheme.colorScheme.onPrimary)
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
fun ModelSpinner(modifier: Modifier = Modifier, type: String = "Gemini", onModelSelected: (String) -> Unit = {}) {
    var expanded by rememberSaveable {
        mutableStateOf(false)
    }
    val modelsList = type.let {
        if(it=="Gemini"){
            Constants.geminiModels
        }
        else{
            Constants.openAIModels
        }
    }
    var selectedModel by rememberSaveable {
        mutableStateOf(modelsList[0])
    }
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
                text = selectedModel,
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
                        text = { Text(item) },
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
fun AppBar(title: String = "Gemini", onClear: (String?) -> Unit = {}, onHistory: () -> Unit = {}) {
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
            }
        }
    )
}

@Preview
@Composable
fun BottomTabBar(modifier: Modifier = Modifier, navController: NavHostController = rememberNavController()){
    BottomAppBar{
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        bottomTabs.forEach { item ->
            BottomNavigationItem(
                icon = {
                    androidx.compose.material.Icon(
                        item.icon,
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
                selectedContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                unselectedContentColor = Color.Gray,
            )
        }
    }
}