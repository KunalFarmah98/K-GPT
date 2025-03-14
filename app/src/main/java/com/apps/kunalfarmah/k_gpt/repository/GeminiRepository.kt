package com.apps.kunalfarmah.k_gpt.repository

import com.apps.kunalfarmah.k_gpt.network.model.gemini.GeminiRequest
import com.apps.kunalfarmah.k_gpt.network.model.gemini.GeminiResponse
import com.apps.kunalfarmah.k_gpt.network.api.GeminiApi
import javax.inject.Inject

class GeminiRepository @Inject constructor(private val geminiApi: GeminiApi) {
    suspend fun generateContent(model: String, request: GeminiRequest): GeminiResponse {
        val response = geminiApi.generateContent(model = model, body = request)
        response.let {
            return if(it.isSuccessful){
                it.body() ?: GeminiResponse()
            } else{
                GeminiResponse()
            }
        }
    }
}