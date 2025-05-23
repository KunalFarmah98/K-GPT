package com.apps.kunalfarmah.k_gpt.network.api

import com.apps.kunalfarmah.k_gpt.BuildConfig
import com.apps.kunalfarmah.k_gpt.network.model.gemini.GeminiImageGenerationRequest
import com.apps.kunalfarmah.k_gpt.network.model.gemini.GeminiImageResponse
import com.apps.kunalfarmah.k_gpt.network.model.gemini.GeminiRequest
import com.apps.kunalfarmah.k_gpt.network.model.gemini.GeminiResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Path

interface GeminiApi {
    // incorporate :generateContent as a part of custom path to prevent retrofit from confusing the ':'
    @POST("models/{model}")
    suspend fun generateContent(
        @Path("model") model: String,
        @Query("key") key: String = BuildConfig.GEMINI_API_KEY,
        @Body body: GeminiRequest
    ): Response<GeminiResponse>

    @POST("models/{model}")
    suspend fun generateImage(
        @Path("model") model: String,
        @Query("key") key: String = BuildConfig.GEMINI_API_KEY,
        @Body body: GeminiImageGenerationRequest
    ): Response<GeminiImageResponse>
}