package com.apps.kunalfarmah.k_gpt.viewmodel

import android.R
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apps.kunalfarmah.k_gpt.Constants
import com.apps.kunalfarmah.k_gpt.OpenAIModels
import com.apps.kunalfarmah.k_gpt.data.Message
import com.apps.kunalfarmah.k_gpt.network.model.OpenAIRequest
import com.apps.kunalfarmah.k_gpt.repository.OpenAIRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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

//        viewModelScope.launch {
//            _isLoading.value = true
//            _messages.value = _messages.value + Message(isUser = true, text = request)
//            delay(2000)
//            _isLoading.value = false
//            _messages.value = _messages.value + Message(isUser = false, text = "Hello there")
//        }


        viewModelScope.launch {
            _isLoading.value = true
            _messages.value = _messages.value + Message(isUser = true, text = request)
            val response = networkRepository.generateContent(openAIRequest)
            var message = if(response.choices.isEmpty()){
                Message(isUser = false, text = "Something went wrong")
            } else{
                Message(isUser = false, text = response.choices[0].message.content.trim())
            }
            _isLoading.value = false
            _messages.value = _messages.value + message
        }
    }

}