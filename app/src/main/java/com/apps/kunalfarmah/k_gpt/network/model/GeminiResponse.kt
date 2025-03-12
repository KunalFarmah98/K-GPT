package com.apps.kunalfarmah.k_gpt.network.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GeminiResponse(
    @SerialName("candidates")
    val candidates: List<Candidate> = listOf(),
    @SerialName("modelVersion")
    val modelVersion: String = "",
    @SerialName("usageMetadata")
    val usageMetadata: UsageMetadata = UsageMetadata()
) {
    @Serializable
    data class Candidate(
        @SerialName("avgLogprobs")
        val avgLogprobs: Double = 0.0,
        @SerialName("citationMetadata")
        val citationMetadata: CitationMetadata = CitationMetadata(),
        @SerialName("content")
        val content: Content = Content(),
        @SerialName("finishReason")
        val finishReason: String = ""
    ) {
        @Serializable
        data class CitationMetadata(
            @SerialName("citationSources")
            val citationSources: List<CitationSource> = listOf()
        ) {
            @Serializable
            data class CitationSource(
                @SerialName("endIndex")
                val endIndex: Int = 0,
                @SerialName("startIndex")
                val startIndex: Int = 0,
                @SerialName("uri")
                val uri: String = ""
            )
        }

        @Serializable
        data class Content(
            @SerialName("parts")
            val parts: List<Part> = listOf(),
            @SerialName("role")
            val role: String = ""
        ) {
            @Serializable
            data class Part(
                @SerialName("text")
                val text: String = ""
            )
        }
    }

    @Serializable
    data class UsageMetadata(
        @SerialName("candidatesTokenCount")
        val candidatesTokenCount: Int = 0,
        @SerialName("candidatesTokensDetails")
        val candidatesTokensDetails: List<CandidatesTokensDetail> = listOf(),
        @SerialName("promptTokenCount")
        val promptTokenCount: Int = 0,
        @SerialName("promptTokensDetails")
        val promptTokensDetails: List<PromptTokensDetail> = listOf(),
        @SerialName("totalTokenCount")
        val totalTokenCount: Int = 0
    ) {
        @Serializable
        data class CandidatesTokensDetail(
            @SerialName("modality")
            val modality: String = "",
            @SerialName("tokenCount")
            val tokenCount: Int = 0
        )

        @Serializable
        data class PromptTokensDetail(
            @SerialName("modality")
            val modality: String = "",
            @SerialName("tokenCount")
            val tokenCount: Int = 0
        )
    }
}