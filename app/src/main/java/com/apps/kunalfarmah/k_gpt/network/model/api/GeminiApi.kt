package com.apps.kunalfarmah.k_gpt.network.model.api

import com.apps.kunalfarmah.k_gpt.BuildConfig
import com.apps.kunalfarmah.k_gpt.network.model.GeminiRequest
import com.apps.kunalfarmah.k_gpt.network.model.GeminiResponse
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
        @Query("key") key: String = BuildConfig.API_KEY,
        @Body body: GeminiRequest
    ): Response<GeminiResponse>
}