package com.apps.kunalfarmah.k_gpt.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.apps.kunalfarmah.k_gpt.ui.screens.GeminiScreen
import com.apps.kunalfarmah.k_gpt.ui.screens.OpenAIScreen
import com.apps.kunalfarmah.k_gpt.ui.screens.Screens

@Composable
fun AppNavigator(navController: NavHostController, modifier: Modifier){
    var textMode by remember {
        mutableStateOf(true)
    }
    NavHost (navController = navController, startDestination = Screens.GeminiChatScreen::class){
        composable<Screens.GeminiChatScreen>{
            GeminiScreen(modifier = modifier, textMode = textMode, setTextMode = {textMode = it})
        }
        composable<Screens.OpenAIChatScreen>{
            OpenAIScreen(modifier = modifier, textMode = textMode, setTextMode = {textMode = it})
        }
    }
}