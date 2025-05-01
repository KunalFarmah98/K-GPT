package com.apps.kunalfarmah.k_gpt.viewmodel

import androidx.lifecycle.viewModelScope
import com.apps.kunalfarmah.k_gpt.data.Message
import com.apps.kunalfarmah.k_gpt.network.model.Event
import com.apps.kunalfarmah.k_gpt.network.model.openAI.OpenAIImageGenerationRequest
import com.apps.kunalfarmah.k_gpt.network.model.openAI.OpenAIRequest
import com.apps.kunalfarmah.k_gpt.repository.OpenAIRepository
import com.apps.kunalfarmah.k_gpt.util.Util.getDate
import com.apps.kunalfarmah.k_gpt.viewmodel.base.ChatViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class OpenAIViewModel @Inject constructor(private val networkRepository: OpenAIRepository): ChatViewModel(networkRepository) {
    override fun generateResponse(model: String, request: String, maxTokens: Int?) {
        val openAIRequest = OpenAIRequest(
            model = model,
            max_tokens = maxTokens,
            messages = listOf(
                OpenAIRequest.Message(
                    role = "user",
                    content = maxTokens.let { tokens ->
                        if(tokens != null && tokens > 0){
                            request.plus(" in under $tokens tokens")
                        }
                        else{
                            request
                        }
                    }
                )
            )
        )

        viewModelScope.launch {
            val userMessage = Message(
                isUser = true,
                text = request,
                platform = "OpenAI",
                firstMessageInDay = (_messages.value.isEmpty() || getDate(_messages.value.last().time) != getDate(
                    Date().time
                ))
            )
            _isLoading.value = true
            _messages.value = _messages.value + userMessage
            val response = networkRepository.generateContent(openAIRequest)
            var finishReason = ""
            var message = if(response.choices.isEmpty()){
                Message(isUser = false, text = "Something went wrong", platform = "OpenAI")
            } else{
                val messageResponse = response.choices[0].message
                finishReason = response.choices[0].finishReason
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
            if(finishReason == "length"){
                _alerts.emit(Event.LimitExceeded("Response token limit exceeded"))
            }
        }
    }

    override fun generateImage(
        model: String,
        request: String,
        maxTokens: Int?
    ) {
        val openAIRequest = OpenAIImageGenerationRequest(
            model = model,
            prompt = request,
            n = 1
        )

        viewModelScope.launch {
            val userMessage = Message(
                isUser = true,
                text = request,
                platform = "OpenAI",
                firstMessageInDay = (_messages.value.isEmpty() || getDate(_messages.value.last().time) != getDate(
                    Date().time
                ))
            )
            _isLoading.value = true
            _messages.value = _messages.value + userMessage
            val response = networkRepository.generateImage(openAIRequest)
            var message = Message(isUser = false, text = "Something went wrong", platform = "OpenAI")
            // TODO: get actual response and check
            _isLoading.value = false
            _messages.value = _messages.value + message
            networkRepository.insertMessage(userMessage)
            networkRepository.insertMessage(message)
        }
    }
}