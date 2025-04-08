package com.apps.kunalfarmah.k_gpt.network.model.gemini

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InlineData(
    @SerialName("data")
    val `data`: String = "",
    @SerialName("mime_type")
    val mimeType: String = ""
)

@Serializable
data class Part(
    @SerialName("inline_data")
    val inlineData: InlineData ?= null,
    @SerialName("text")
    val text: String ?= null
)

@Serializable
data class Content(
    @SerialName("parts")
    val parts: List<Part> = listOf()
)
@Serializable
data class GenerationConfig(
    @SerialName("responseModalities")
    val responseModalities: List<String> = listOf()
)


@Serializable
data class GeminiRequest(
    @SerialName("contents")
    val contents: List<Content> = listOf()
)

@Serializable
data class GeminiImageGenerationRequest(
    @SerialName("contents")
    val contents: List<Content> = listOf(),
    @SerialName("generationConfig")
    val generationConfig: GenerationConfig = GenerationConfig()
)