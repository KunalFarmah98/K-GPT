package com.apps.kunalfarmah.k_gpt.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apps.kunalfarmah.k_gpt.Constants
import com.apps.kunalfarmah.k_gpt.network.model.GeminiRequest
import com.apps.kunalfarmah.k_gpt.repository.GeminiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GeminiViewModel @Inject constructor(private val networkRepository: GeminiRepository): ViewModel() {

    fun generateRequest(models: Constants.GeminiModels = Constants.GeminiModels.GEMINI_2_0_FLASH_LITE, request: String) {
        val url = "${models.modelName}:generateContent"
        val geminiRequest = GeminiRequest(
            contents = listOf(
                GeminiRequest.Content(
                    parts = listOf(
                        GeminiRequest.Content.Part(
                            text = request
                        )
                    )
                )
            )
        )

        viewModelScope.launch {
            networkRepository.generateContent(url, geminiRequest)
        }
    }

}