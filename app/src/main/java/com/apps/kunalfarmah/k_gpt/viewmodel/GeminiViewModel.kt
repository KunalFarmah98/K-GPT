package com.apps.kunalfarmah.k_gpt.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apps.kunalfarmah.k_gpt.GeminiModels
import com.apps.kunalfarmah.k_gpt.data.Message
import com.apps.kunalfarmah.k_gpt.network.model.gemini.GeminiRequest
import com.apps.kunalfarmah.k_gpt.repository.GeminiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GeminiViewModel @Inject constructor(private val networkRepository: GeminiRepository): ViewModel() {

    private val _messages = MutableStateFlow<List<Message>>(listOf())
    val messages = _messages.asStateFlow()

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading = _isLoading.asStateFlow()

    fun generateRequest(model: String = GeminiModels.GEMINI_2_0_FLASH.name, request: String) {
        val modelName = "$model:generateContent"
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
            _isLoading.value = true
            _messages.value = _messages.value + Message(isUser = true, text = request)
            val response = networkRepository.generateContent(modelName, geminiRequest)
            var message = if(response.candidates.isEmpty()){
                Message(isUser = false, text = "Something went wrong")
            } else{
                val messageResponse = response.candidates[0]
                Message(
                    isUser = false,
                    text = messageResponse.content.parts[0].text.trim(),
                    citations = messageResponse.citationMetadata.citationSources.map { it.uri })
            }
            _isLoading.value = false
            _messages.value = _messages.value + message
        }
    }

}