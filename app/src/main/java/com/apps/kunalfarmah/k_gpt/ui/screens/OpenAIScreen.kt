package com.apps.kunalfarmah.k_gpt.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.apps.kunalfarmah.k_gpt.viewmodel.OpenAIViewModel

@Preview
@Composable
fun OpenAIScreen(modifier: Modifier = Modifier, viewModel: OpenAIViewModel = hiltViewModel(), textMode: Boolean = true, setTextMode: (Boolean) -> Unit = {}) {
   ChatScreen(modifier = modifier, viewModel = viewModel, textMode = textMode, setTextMode = setTextMode, platform = "OpenAI")
}