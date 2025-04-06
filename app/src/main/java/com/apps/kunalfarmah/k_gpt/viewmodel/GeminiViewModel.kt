package com.apps.kunalfarmah.k_gpt.viewmodel

import androidx.lifecycle.viewModelScope
import com.apps.kunalfarmah.k_gpt.data.Message
import com.apps.kunalfarmah.k_gpt.network.model.gemini.GeminiImageGenerationRequest
import com.apps.kunalfarmah.k_gpt.network.model.gemini.GeminiRequest
import com.apps.kunalfarmah.k_gpt.repository.GeminiRepository
import com.apps.kunalfarmah.k_gpt.util.Util.getDate
import com.apps.kunalfarmah.k_gpt.viewmodel.base.ChatViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class GeminiViewModel @Inject constructor(private val networkRepository: GeminiRepository): ChatViewModel(networkRepository) {
    override fun generateRequest(model: String, request: String, maxTokens: Int?) {
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
            val userMessage = Message(
                isUser = true,
                text = request,
                platform = "Gemini",
                firstMessageInDay = (_messages.value.isEmpty() || getDate(_messages.value.last().time) != getDate(
                    Date().time
                ))
            )
            _isLoading.value = true
            _messages.value = _messages.value + userMessage
            val response = networkRepository.generateContent(modelName, geminiRequest)
            var message = if(response.candidates.isEmpty()){
                Message(isUser = false, text = "Something went wrong", platform = "Gemini")
            } else{
                val messageResponse = response.candidates[0]
                Message(
                    isUser = false,
                    text = messageResponse.content.parts[0].text.trim(),
                    citations = messageResponse.citationMetadata.citationSources.map { it.uri },
                    platform = "Gemini"
                )
            }
            _isLoading.value = false
            _messages.value = _messages.value + message
            networkRepository.insertMessage(userMessage)
            networkRepository.insertMessage(message)
        }
    }

    override fun generateImage(model: String, request: String, maxTokens: Int?) {
        val modelName = "$model:generateContent"
        val geminiImageRequest = GeminiImageGenerationRequest(
            contents = listOf(
                GeminiImageGenerationRequest.Content(
                    parts = listOf(
                        GeminiImageGenerationRequest.Content.Part(text = request)
                    )
                )
            ),
            generationConfig = GeminiImageGenerationRequest.GenerationConfig(
                responseModalities = listOf("Text", "Image")
            )
        )

        viewModelScope.launch {
            val userMessage = Message(
                isUser = true,
                text = request,
                platform = "Gemini",
                firstMessageInDay = (_messages.value.isEmpty() || getDate(_messages.value.last().time) != getDate(
                    Date().time
                ))
            )
            _isLoading.value = true
            _messages.value = _messages.value + userMessage
            val response = networkRepository.generateImage(modelName, geminiImageRequest)
            var message = if(response.candidates.isEmpty()){
                Message(isUser = false, text = "Something went wrong", platform = "Gemini")
            }
            else{
                if(!response.errorMessage.isNullOrEmpty()){
                    Message(isUser = false, text = response.errorMessage, platform = "Gemini")
                }
                else {
                    val messageResponse = response.candidates[0].content.parts[0]
                    Message(
                        isUser = false,
                        platform = "Gemini",
                        isImage = true,
                        imageData = messageResponse.inlineData.data,
                        mimeType = messageResponse.inlineData.mimeType
                    )
                }
            }
            _isLoading.value = false
            _messages.value = _messages.value + message
            networkRepository.insertMessage(userMessage)
            networkRepository.insertMessage(message)
        }
    }

}