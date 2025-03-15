package com.apps.kunalfarmah.k_gpt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.apps.kunalfarmah.k_gpt.navigation.AppNavigator
import com.apps.kunalfarmah.k_gpt.ui.components.AppBar
import com.apps.kunalfarmah.k_gpt.ui.components.BottomTabBar
import com.apps.kunalfarmah.k_gpt.ui.theme.KGPTTheme
import com.apps.kunalfarmah.k_gpt.viewmodel.GeminiViewModel
import com.apps.kunalfarmah.k_gpt.viewmodel.OpenAIViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val geminiViewModel: GeminiViewModel by viewModels()
        val openAIViewModel: OpenAIViewModel by viewModels()
        enableEdgeToEdge()
        setContent {
            KGPTTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize(),
                    topBar = {
                        AppBar(title = resources.getString(R.string.app_name), onClear = {
                            it.let{
                                if(it == null){
                                    geminiViewModel.deleteAllMessages()
                                    openAIViewModel.deleteAllMessages()
                                }
                                if(it=="Gemini"){
                                    geminiViewModel.deleteAllMessages()
                                }
                                else{
                                    openAIViewModel.deleteAllMessages()
                                }
                            }
                        },
                            onHistory = {
                                openAIViewModel.getAllMessages()
                                geminiViewModel.getAllMessages()
                            }
                        )
                    },
                    bottomBar = {
                        BottomTabBar(navController = navController)
                    }
                ) { innerPadding ->
                    AppNavigator(navController, Modifier.padding(innerPadding))
                }
            }
        }
    }
}