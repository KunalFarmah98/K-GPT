package com.apps.kunalfarmah.k_gpt

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

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

    fun initializeSaveImageLauncher(
        caller: ActivityResultCaller,
        bitmap: Bitmap,
        mimeType: String,
    ): ActivityResultLauncher<Intent> {
        return caller.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.data?.let { uri ->
                    //Now we have the URI where we need to write the bitmap to, which may be in a different location than downloads (API < 29)
                   lifecycleScope.launch(Dispatchers.IO) { // Launch a coroutine on the IO dispatcher
                        try {
                            this@MainActivity.contentResolver.openOutputStream(uri)
                                ?.use { outputStream ->
                                    when (mimeType) {
                                        "image/jpeg" -> bitmap.compress(
                                            Bitmap.CompressFormat.JPEG,
                                            100,
                                            outputStream
                                        )

                                        "image/png" -> bitmap.compress(
                                            Bitmap.CompressFormat.PNG,
                                            100,
                                            outputStream
                                        )
                                    }
                                }
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Image saved successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        } catch (e: IOException) {
                            e.printStackTrace()
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Failed to save image",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                    }

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