package com.apps.kunalfarmah.k_gpt.network.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GeminiRequest(
    @SerialName("contents")
    val contents: List<Content> = listOf()
) {
    @Serializable
    data class Content(
        @SerialName("parts")
        val parts: List<Part> = listOf()
    ) {
        @Serializable
        data class Part(
            @SerialName("text")
            val text: String = ""
        )
    }
}