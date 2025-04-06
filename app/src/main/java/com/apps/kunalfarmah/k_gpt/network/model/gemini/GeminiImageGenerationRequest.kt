package com.apps.kunalfarmah.k_gpt.network.model.gemini


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GeminiImageGenerationRequest(
    @SerialName("contents")
    val contents: List<Content> = listOf(),
    @SerialName("generationConfig")
    val generationConfig: GenerationConfig = GenerationConfig()
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

    @Serializable
    data class GenerationConfig(
        @SerialName("responseModalities")
        val responseModalities: List<String> = listOf()
    )
}