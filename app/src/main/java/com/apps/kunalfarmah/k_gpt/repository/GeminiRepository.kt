package com.apps.kunalfarmah.k_gpt.repository

import android.util.Log
import com.apps.kunalfarmah.k_gpt.network.model.GeminiRequest
import com.apps.kunalfarmah.k_gpt.network.model.api.GeminiApi
import javax.inject.Inject

class GeminiRepository @Inject constructor(private val geminiApi: GeminiApi) {
    suspend fun generateContent(model: String, request: GeminiRequest) {
        val response = geminiApi.generateContent(model = model, body = request)
        response.let {
            if(it.isSuccessful){
                Log.d("GeminiRepository", "generateContent: ${it.body().toString()}")
            }
            else{
                Log.e("GeminiRepository", "generateContent: ${it.errorBody()}")
            }
        }
    }
}