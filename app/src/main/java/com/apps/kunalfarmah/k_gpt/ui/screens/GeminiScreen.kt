package com.apps.kunalfarmah.k_gpt.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.apps.kunalfarmah.k_gpt.viewmodel.GeminiViewModel

@Preview
@Composable
fun GeminiScreen(modifier: Modifier = Modifier, viewModel: GeminiViewModel = hiltViewModel()) {
    ChatScreen(modifier, viewModel, "Gemini")
}