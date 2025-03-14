package com.apps.kunalfarmah.k_gpt.network.api

import com.apps.kunalfarmah.k_gpt.BuildConfig
import com.apps.kunalfarmah.k_gpt.network.model.openAI.OpenAIRequest
import com.apps.kunalfarmah.k_gpt.network.model.openAI.OpenAIResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface OpenAIApi {
    @POST("chat/completions")
    suspend fun generateContent(
        @Header("Authorization") auth:String = "Bearer ${BuildConfig.OPEN_AI_API_KEY}",
        @Header("content-type") contentType: String =  "application/json",
        @Body body: OpenAIRequest
    ): Response<OpenAIResponse>
}