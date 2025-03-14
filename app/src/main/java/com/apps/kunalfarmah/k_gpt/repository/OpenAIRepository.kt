package com.apps.kunalfarmah.k_gpt.repository

import com.apps.kunalfarmah.k_gpt.network.model.openAI.OpenAIRequest
import com.apps.kunalfarmah.k_gpt.network.model.openAI.OpenAIResponse
import com.apps.kunalfarmah.k_gpt.network.api.OpenAIApi
import javax.inject.Inject

class OpenAIRepository @Inject constructor(private val openAIApi: OpenAIApi) {
    suspend fun generateContent(request: OpenAIRequest): OpenAIResponse {
        val response = openAIApi.generateContent(body = request)
        response.let {
            return if(it.isSuccessful){
                it.body() ?: OpenAIResponse()
            } else{
                OpenAIResponse()
            }
        }
    }
}