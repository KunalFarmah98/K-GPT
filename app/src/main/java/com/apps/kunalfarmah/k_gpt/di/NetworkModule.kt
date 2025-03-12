package com.apps.kunalfarmah.k_gpt.di

import com.apps.kunalfarmah.k_gpt.Constants.GEMINI_BASE_URL
import com.apps.kunalfarmah.k_gpt.network.model.api.GeminiApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Provides
    fun provideGeminiRetrofit() : Retrofit {
        return Retrofit.Builder()
            .baseUrl(GEMINI_BASE_URL)
            .addConverterFactory(Json.asConverterFactory(
                "application/json; charset=UTF8".toMediaType()))
            .build()
    }

    @Provides
    fun provideGeminiApi(retrofit: Retrofit): GeminiApi {
        return retrofit.create(GeminiApi::class.java)
    }

}