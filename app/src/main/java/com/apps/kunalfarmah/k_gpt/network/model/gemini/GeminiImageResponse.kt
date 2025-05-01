package com.apps.kunalfarmah.k_gpt.network.model.gemini


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GeminiImageResponse(
    @SerialName("candidates")
    val candidates: List<Candidate> = listOf(),
    @SerialName("modelVersion")
    val modelVersion: String = "",
    @SerialName("usageMetadata")
    val usageMetadata: UsageMetadata = UsageMetadata(),
    val errorMessage: String ?= null
) {
    @Serializable
    data class Candidate(
        @SerialName("content")
        val content: Content = Content(),
        @SerialName("finishReason")
        val finishReason: String = "",
        @SerialName("index")
        val index: Int = 0
    ) {
        @Serializable
        data class Content(
            @SerialName("parts")
            val parts: List<Part> = listOf(),
            @SerialName("role")
            val role: String = ""
        ) {
            @Serializable
            data class Part(
                @SerialName("inlineData")
                val inlineData: InlineData? = null,
                @SerialName("text")
                val text: String? = null
            ) {
                @Serializable
                data class InlineData(
                    @SerialName("data")
                    val `data`: String = "",
                    @SerialName("mimeType")
                    val mimeType: String = ""
                )
            }
        }
    }

    @Serializable
    data class UsageMetadata(
        @SerialName("promptTokenCount")
        val promptTokenCount: Int = 0,
        @SerialName("promptTokensDetails")
        val promptTokensDetails: List<PromptTokensDetail> = listOf(),
        @SerialName("totalTokenCount")
        val totalTokenCount: Int = 0
    ) {
        @Serializable
        data class PromptTokensDetail(
            @SerialName("modality")
            val modality: String = "",
            @SerialName("tokenCount")
            val tokenCount: Int = 0
        )
    }
}