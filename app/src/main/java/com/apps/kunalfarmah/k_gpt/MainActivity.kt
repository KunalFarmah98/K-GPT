package com.apps.kunalfarmah.k_gpt

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.apps.kunalfarmah.k_gpt.navigation.AppNavigator
import com.apps.kunalfarmah.k_gpt.ui.components.AppBar
import com.apps.kunalfarmah.k_gpt.ui.components.BottomTabBar
import com.apps.kunalfarmah.k_gpt.ui.theme.KGPTTheme
import com.apps.kunalfarmah.k_gpt.util.SettingsKeys
import com.apps.kunalfarmah.k_gpt.viewmodel.GeminiViewModel
import com.apps.kunalfarmah.k_gpt.viewmodel.OpenAIViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

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
                var backStackEntry = navController.currentBackStackEntryAsState()
                Scaffold(
                    modifier = Modifier.imePadding(),
                    topBar = {
                        AppBar(
                            title = resources.getString(R.string.app_name),
                            backStackEntry = backStackEntry.value,
                            onClear = {
                                it.let {
                                    if (it == null) {
                                        geminiViewModel.deleteAllMessages(it)
                                        openAIViewModel.deleteAllMessages(it)
                                    }
                                    if (it == "Gemini") {
                                        geminiViewModel.deleteAllMessages(it)
                                    } else {
                                        openAIViewModel.deleteAllMessages(it)
                                    }
                                }
                            },
                            onHistory = {
                                openAIViewModel.getAllMessages("OpenAI")
                                geminiViewModel.getAllMessages("Gemini")
                            },
                            onTokenSettings = {
                                openAIViewModel.toggleMaxTokensDialog(true)
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

// DataStore extension functions
suspend fun DataStore<Preferences>.setMaxTokens(maxTokens: Int) {
    edit { settings ->
        settings[SettingsKeys.MAX_TOKENS] = maxTokens
    }
}

suspend fun DataStore<Preferences>.getMaxTokens(): Int? {
    val preferences = data.first()
    return preferences[SettingsKeys.MAX_TOKENS]
}