package com.apps.kunalfarmah.k_gpt.network.model.api

import com.apps.kunalfarmah.k_gpt.network.model.GeminiRequest
import com.apps.kunalfarmah.k_gpt.network.model.GeminiResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url

interface GeminiApi {
    @POST
    fun generateContent(@Url url: String, @Body request: GeminiRequest): Response<GeminiResponse>
}