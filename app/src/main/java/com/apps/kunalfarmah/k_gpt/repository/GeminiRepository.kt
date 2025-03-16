package com.apps.kunalfarmah.k_gpt.repository

import com.apps.kunalfarmah.k_gpt.db.MessageDAO
import com.apps.kunalfarmah.k_gpt.network.model.gemini.GeminiRequest
import com.apps.kunalfarmah.k_gpt.network.model.gemini.GeminiResponse
import com.apps.kunalfarmah.k_gpt.network.api.GeminiApi
import javax.inject.Inject

class GeminiRepository @Inject constructor(private val geminiApi: GeminiApi, messageDAO: MessageDAO) : MessagesRepository(messageDAO) {
    suspend fun generateContent(model: String, request: GeminiRequest): GeminiResponse {
        return try {
            val response = geminiApi.generateContent(model = model, body = request)
            response.let {
                if (it.isSuccessful) {
                    it.body() ?: GeminiResponse()
                } else {
                    GeminiResponse()
                }
            }
        }
        catch (_: Exception){
            GeminiResponse()
        }
    }
}