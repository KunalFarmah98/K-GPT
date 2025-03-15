package com.apps.kunalfarmah.k_gpt.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apps.kunalfarmah.k_gpt.OpenAIModels
import com.apps.kunalfarmah.k_gpt.data.Message
import com.apps.kunalfarmah.k_gpt.network.model.openAI.OpenAIRequest
import com.apps.kunalfarmah.k_gpt.repository.OpenAIRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OpenAIViewModel @Inject constructor(private val networkRepository: OpenAIRepository): ViewModel() {

    private val _messages = MutableStateFlow<List<Message>>(listOf())
    val messages = _messages.asStateFlow()

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading = _isLoading.asStateFlow()

    fun generateRequest(model: String = OpenAIModels.GPT_4O_MINI.modelName, request: String) {

        val openAIRequest = OpenAIRequest(
            model = model,
            messages = listOf(
                OpenAIRequest.Message(
                    role = "user",
                    content = request
                )
            )
        )

        viewModelScope.launch {
            val userMessage = Message(isUser = true, text = request, platform = "OpenAI")
            _isLoading.value = true
            _messages.value = _messages.value + userMessage
            val response = networkRepository.generateContent(openAIRequest)
            var message = if(response.choices.isEmpty()){
                Message(isUser = false, text = "Something went wrong", platform = "OpenAI")
            } else{
                val messageResponse = response.choices[0].message
                Message(
                    isUser = false,
                    text = messageResponse.content.trim(),
                    citations = messageResponse.annotations,
                    platform = "OpenAI"
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
            _messages.value = networkRepository.getAllMessages("OpenAI")
        }
    }

    fun deleteAllMessages(){
        viewModelScope.launch(Dispatchers.IO) {
            networkRepository.deleteAllMessages("OpenAI")
            _messages.value = listOf()
        }
    }

}