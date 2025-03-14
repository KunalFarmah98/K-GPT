package com.apps.kunalfarmah.k_gpt.network.model.openAI


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OpenAIResponse(
    @SerialName("choices")
    val choices: List<Choice> = listOf(),
    @SerialName("created")
    val created: Int = 0,
    @SerialName("id")
    val id: String = "",
    @SerialName("model")
    val model: String = "",
    @SerialName("object")
    val objectX: String = "",
    @SerialName("service_tier")
    val serviceTier: String = "",
    @SerialName("system_fingerprint")
    val systemFingerprint: String = "",
    @SerialName("usage")
    val usage: Usage = Usage()
) {
    @Serializable
    data class Choice(
        @SerialName("finish_reason")
        val finishReason: String = "",
        @SerialName("index")
        val index: Int = 0,
        @SerialName("logprobs")
        val logprobs: String? = "",
        @SerialName("message")
        val message: Message = Message()
    ) {
        @Serializable
        data class Message(
            @SerialName("annotations")
            val annotations: List<String> = listOf(),
            @SerialName("content")
            val content: String = "",
            @SerialName("refusal")
            val refusal: String? = "",
            @SerialName("role")
            val role: String = ""
        )
    }

    @Serializable
    data class Usage(
        @SerialName("completion_tokens")
        val completionTokens: Int = 0,
        @SerialName("completion_tokens_details")
        val completionTokensDetails: CompletionTokensDetails = CompletionTokensDetails(),
        @SerialName("prompt_tokens")
        val promptTokens: Int = 0,
        @SerialName("prompt_tokens_details")
        val promptTokensDetails: PromptTokensDetails = PromptTokensDetails(),
        @SerialName("total_tokens")
        val totalTokens: Int = 0
    ) {
        @Serializable
        data class CompletionTokensDetails(
            @SerialName("accepted_prediction_tokens")
            val acceptedPredictionTokens: Int = 0,
            @SerialName("audio_tokens")
            val audioTokens: Int = 0,
            @SerialName("reasoning_tokens")
            val reasoningTokens: Int = 0,
            @SerialName("rejected_prediction_tokens")
            val rejectedPredictionTokens: Int = 0
        )

        @Serializable
        data class PromptTokensDetails(
            @SerialName("audio_tokens")
            val audioTokens: Int = 0,
            @SerialName("cached_tokens")
            val cachedTokens: Int = 0
        )
    }
}