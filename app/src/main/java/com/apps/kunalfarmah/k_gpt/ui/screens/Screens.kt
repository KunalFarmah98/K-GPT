package com.apps.kunalfarmah.k_gpt.ui.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable


data class BottomTabs<T : Any>(val name: String, val route: T, val icon: ImageVector)

val bottomTabs = listOf(
    BottomTabs("Gemini", Screens.GeminiChatScreen, Icons.Filled.Home),
    BottomTabs("OpenAI", Screens.OpenAIChatScreen, Icons.Filled.Search),
)

sealed class Screens{
    @Serializable
    object GeminiChatScreen: Screens()
    @Serializable
    object OpenAIChatScreen: Screens()
}