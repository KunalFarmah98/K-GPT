package com.apps.kunalfarmah.k_gpt.repository

import com.apps.kunalfarmah.k_gpt.network.model.GeminiRequest
import com.apps.kunalfarmah.k_gpt.network.model.api.GeminiApi
import javax.inject.Inject

class GeminiRepository @Inject constructor(private val geminiApi: GeminiApi) {
    suspend fun generateContent(url: String, request: GeminiRequest) {
        val response = geminiApi.generateContent(url, request)
    }
}