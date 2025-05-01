package com.apps.kunalfarmah.k_gpt.network.model.openAI


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OpenAIImageGenerationRequest(
    @SerialName("model")
    val model: String = "",
    @SerialName("n")
    val n: Int = 0,
    @SerialName("prompt")
    val prompt: String = "",
    @SerialName("size")
    val size: String = "",
    @SerialName("response_format")
    val responseFormat: String = "b64_json"
)