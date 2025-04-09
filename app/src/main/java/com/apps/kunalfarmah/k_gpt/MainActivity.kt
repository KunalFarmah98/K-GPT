package com.apps.kunalfarmah.k_gpt

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.apps.kunalfarmah.k_gpt.navigation.AppNavigator
import com.apps.kunalfarmah.k_gpt.network.model.Event
import com.apps.kunalfarmah.k_gpt.ui.components.AppBar
import com.apps.kunalfarmah.k_gpt.ui.components.BottomTabBar
import com.apps.kunalfarmah.k_gpt.ui.theme.KGPTTheme
import com.apps.kunalfarmah.k_gpt.util.SettingsKeys
import com.apps.kunalfarmah.k_gpt.util.Util
import com.apps.kunalfarmah.k_gpt.viewmodel.GeminiViewModel
import com.apps.kunalfarmah.k_gpt.viewmodel.OpenAIViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.OutputStream

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    lateinit var saveImageLauncher: ActivityResultLauncher<Intent>
    lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val geminiViewModel: GeminiViewModel by viewModels()
        val openAIViewModel: OpenAIViewModel by viewModels()
        enableEdgeToEdge()
        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                lifecycleScope.launch(Dispatchers.Default) {
                    val mimeType = Util.getMimeTypeFromUri(this@MainActivity, uri) ?: "image/jpeg"
                    val base64Data = Util.uriToBase64(this@MainActivity, uri, mimeType) ?: ""
                    geminiViewModel.setUploadImageData(base64Data = base64Data, mimeType = mimeType)
                    geminiViewModel.uploadImageToMessages(base64Data = base64Data, mimeType = mimeType)
                }
            } else {
                Toast.makeText(this, "No media selected", Toast.LENGTH_SHORT).show()
            }
        }

        saveImageLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    var outputStream: OutputStream? = null
                    result.data?.data?.let { uri ->
                        lifecycleScope.launch(Dispatchers.Default) {
                            geminiViewModel.imageToBeDownloaded.collectLatest {
                                try {
                                    outputStream =
                                        this@MainActivity.contentResolver.openOutputStream(uri)
                                    outputStream?.use { outputStream ->
                                        it.bitmap?.compress(
                                            when (it.mimeType) {
                                                "image/jpeg" -> Bitmap.CompressFormat.JPEG
                                                "image/png" -> Bitmap.CompressFormat.PNG
                                                else -> Bitmap.CompressFormat.JPEG
                                            },
                                            100,
                                            outputStream
                                        )
                                    }
                                    it.platform.let {
                                        geminiViewModel.clearDownloadedImageData()
                                        if (it == "Gemini") {
                                            geminiViewModel.alert(Event.Toast("Image saved successfully"))
                                        } else {
                                            openAIViewModel.alert(Event.Toast("Image saved successfully"))
                                        }
                                    }
                                } catch (_: IOException) {
                                    geminiViewModel.clearDownloadedImageData()
                                    it.platform.let {
                                        if (it == "Gemini") {
                                            geminiViewModel.alert(Event.Toast("Failed to save image"))
                                        } else {
                                            openAIViewModel.alert(Event.Toast("Failed to save image"))
                                        }
                                    }
                                }
                            }
                        }.invokeOnCompletion {
                            outputStream?.close()
                        }
                    }
                }
            }

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