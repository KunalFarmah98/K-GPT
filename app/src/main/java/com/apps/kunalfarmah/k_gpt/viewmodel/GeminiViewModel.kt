package com.apps.kunalfarmah.k_gpt.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apps.kunalfarmah.k_gpt.GeminiModels
import com.apps.kunalfarmah.k_gpt.data.Message
import com.apps.kunalfarmah.k_gpt.network.model.gemini.GeminiRequest
import com.apps.kunalfarmah.k_gpt.repository.GeminiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class GeminiViewModel @Inject constructor(private val networkRepository: GeminiRepository): ViewModel() {

    private val _messages = MutableStateFlow<List<Message>>(listOf())
    val messages = _messages.asStateFlow()

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        //clear all older messages than 1 week
        viewModelScope.launch(Dispatchers.IO) {
            networkRepository.deleteOlderMessages(Date().time.minus(7 * 24 * 60 * 60 * 1000))
        }
    }

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
            val userMessage = Message(isUser = true, text = request, platform = "Gemini")
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

    fun getAllMessages(){
        viewModelScope.launch(Dispatchers.IO) {
            _messages.value = networkRepository.getAllMessages("Gemini")
        }
    }

    fun deleteAllMessages(){
        viewModelScope.launch(Dispatchers.IO) {
            networkRepository.deleteAllMessages("Gemini")
            _messages.value = listOf()
        }
    }

}