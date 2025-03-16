package com.apps.kunalfarmah.k_gpt.ui.screens

import com.apps.kunalfarmah.k_gpt.R
import kotlinx.serialization.Serializable


data class BottomTabs<T : Any>(val name: String, val route: T, val iconId: Int)

val bottomTabs = listOf(
    BottomTabs("Gemini", Screens.GeminiChatScreen, R.drawable.ic_gemini),
    BottomTabs("OpenAI", Screens.OpenAIChatScreen, R.drawable.ic_openai),
)

sealed class Screens{
    @Serializable
    object GeminiChatScreen: Screens()
    @Serializable
    object OpenAIChatScreen: Screens()
}