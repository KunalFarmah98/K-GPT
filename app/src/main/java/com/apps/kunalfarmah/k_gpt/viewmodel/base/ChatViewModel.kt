package com.apps.kunalfarmah.k_gpt.viewmodel.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apps.kunalfarmah.k_gpt.data.ImageData
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
    protected val _messages = MutableStateFlow<List<Message>>(listOf())
    val messages = _messages.asStateFlow()

    protected val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading = _isLoading.asStateFlow()

    // history loading state needs to be shared by all screen so we need a replay cache
    protected val _historyLoading = MutableSharedFlow<Event.HistoryLoading>(replay = 1)
    val historyLoading = _historyLoading.asSharedFlow()

    protected val _imageData = MutableStateFlow<ImageData>(ImageData())

    protected val _imageToBeDownloaded = MutableStateFlow<ImageData>(ImageData())
    val imageToBeDownloaded = _imageToBeDownloaded.asStateFlow()

    // alerts are one time events, so no replay cache required here
    protected val _alerts = MutableSharedFlow<Event>()
    val alerts = _alerts.asSharedFlow()

    init {
        //clear all older messages than 1 week
        viewModelScope.launch(Dispatchers.IO) {
            messagesRepository.deleteOlderMessages(Date().time.minus(7 * 24 * 60 * 60 * 1000))
        }
    }

    fun getAllMessages(platform: String){
        viewModelScope.launch(Dispatchers.IO) {
            _historyLoading.emit(Event.HistoryLoading(true))
            _messages.value = messagesRepository.getAllMessages(platform)
            _historyLoading.emit(Event.HistoryLoading(false))
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

    fun setDownloadedImageData(imageData: ImageData){
        _imageToBeDownloaded.value = imageData
    }

    fun setImageData(imageData: ImageData){
        _imageData.value = imageData
    }

    fun alert(event: Event){
        viewModelScope.launch {
            _alerts.emit(event)
        }
    }

    fun setUploadImageData(base64Data: String, mimeType: String){
        _imageData.value = ImageData(base64Data = base64Data, mimeType = mimeType)
    }

    fun clearImageData(){
        _imageData.value = ImageData()
    }

    fun clearDownloadedImageData(){
        _imageToBeDownloaded.value = ImageData()
    }

    fun uploadImageToMessages(base64Data: String, mimeType: String){
        _messages.value +=  Message(
            isUser = true,
            platform = "Gemini",
            isImage = true,
            imageData = base64Data,
            mimeType = mimeType
        )
    }

    abstract fun generateResponse(model: String, request: String, maxTokens: Int? = null)

    abstract fun generateImage(model: String, request: String, maxTokens: Int? = null)

}