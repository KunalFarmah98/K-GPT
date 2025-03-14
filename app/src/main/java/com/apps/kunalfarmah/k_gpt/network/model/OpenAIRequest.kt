package com.apps.kunalfarmah.k_gpt.network.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OpenAIRequest(
    @SerialName("messages")
    val messages: List<Message> = listOf(),
    @SerialName("model")
    val model: String = "",
    @SerialName("store")
    val store: Boolean = false
) {
    @Serializable
    data class Message(
        @SerialName("content")
        val content: String = "",
        @SerialName("role")
        val role: String = ""
    )
}