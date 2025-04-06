package com.apps.kunalfarmah.k_gpt.viewmodel.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apps.kunalfarmah.k_gpt.data.Message
import com.apps.kunalfarmah.k_gpt.network.model.Event
import com.apps.kunalfarmah.k_gpt.repository.base.MessagesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date

abstract class ChatViewModel(private val messagesRepository: MessagesRepository): ViewModel() {
    internal val _messages = MutableStateFlow<List<Message>>(listOf())
    val messages = _messages.asStateFlow()

    internal val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading = _isLoading.asStateFlow()

    internal val _alerts = MutableSharedFlow<Event>()
    val alerts = _alerts.asSharedFlow()

    init {
        //clear all older messages than 1 week
        viewModelScope.launch(Dispatchers.IO) {
            messagesRepository.deleteOlderMessages(Date().time.minus(7 * 24 * 60 * 60 * 1000))
        }
    }

    fun getAllMessages(platform: String){
        viewModelScope.launch(Dispatchers.IO) {
            _messages.value = messagesRepository.getAllMessages(platform)
            if(_messages.value.isEmpty()){
                _alerts.emit(Event.Toast("No $platform History Found"))
            }
        }
    }

    fun deleteAllMessages(platform: String?){
        viewModelScope.launch(Dispatchers.IO) {
            messagesRepository.deleteAllMessages(platform)
            _messages.value = listOf()
        }
    }

    fun toggleMaxTokensDialog(show: Boolean){
        viewModelScope.launch {
            _alerts.emit(Event.MaxTokensDialog(show))
        }
    }

    abstract fun generateRequest(model: String, request: String, maxTokens: Int? = null)

    abstract fun generateImage(model: String, request: String, maxTokens: Int? = null)
}