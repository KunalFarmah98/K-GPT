package com.apps.kunalfarmah.k_gpt.repository

import com.apps.kunalfarmah.k_gpt.db.MessageDAO
import com.apps.kunalfarmah.k_gpt.network.model.openAI.OpenAIRequest
import com.apps.kunalfarmah.k_gpt.network.model.openAI.OpenAIResponse
import com.apps.kunalfarmah.k_gpt.network.api.OpenAIApi
import javax.inject.Inject

class OpenAIRepository @Inject constructor(private val openAIApi: OpenAIApi, messageDAO: MessageDAO) : MessagesRepository(messageDAO) {
    suspend fun generateContent(request: OpenAIRequest): OpenAIResponse {
        return try {
            val response = openAIApi.generateContent(body = request)
            response.let {
                if (it.isSuccessful) {
                    it.body() ?: OpenAIResponse()
                } else {
                    OpenAIResponse()
                }
            }
        }
        catch (_: Exception){
            OpenAIResponse()
        }
    }
}