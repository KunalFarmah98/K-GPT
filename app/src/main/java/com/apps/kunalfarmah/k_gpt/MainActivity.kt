package com.apps.kunalfarmah.k_gpt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.apps.kunalfarmah.k_gpt.navigation.AppNavigator
import com.apps.kunalfarmah.k_gpt.ui.components.AppBar
import com.apps.kunalfarmah.k_gpt.ui.components.BottomTabBar
import com.apps.kunalfarmah.k_gpt.ui.theme.KGPTTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KGPTTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize(),
                    topBar = {
                        AppBar(title = resources.getString(R.string.app_name))
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