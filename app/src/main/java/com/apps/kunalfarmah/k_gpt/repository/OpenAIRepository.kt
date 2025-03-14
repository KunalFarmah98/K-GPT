package com.apps.kunalfarmah.k_gpt.repository

import com.apps.kunalfarmah.k_gpt.network.model.GeminiRequest
import com.apps.kunalfarmah.k_gpt.network.model.GeminiResponse
import com.apps.kunalfarmah.k_gpt.network.model.api.OpenAIApi
import javax.inject.Inject

class OpenAIRepository @Inject constructor(private val openAIApi: OpenAIApi) {
    suspend fun generateContent(model: String, request: GeminiRequest): GeminiResponse {
        val response = openAIApi.generateContent(model = model, body = request)
        response.let {
            return if(it.isSuccessful){
                it.body() ?: GeminiResponse()
            } else{
                GeminiResponse()
            }
        }
    }
}