package com.apps.kunalfarmah.k_gpt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.apps.kunalfarmah.k_gpt.ui.screens.GeminiScreen
import com.apps.kunalfarmah.k_gpt.ui.theme.KGPTTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KGPTTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GeminiScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}