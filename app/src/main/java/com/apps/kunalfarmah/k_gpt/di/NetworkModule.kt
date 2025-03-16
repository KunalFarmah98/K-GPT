package com.apps.kunalfarmah.k_gpt.di

import com.apps.kunalfarmah.k_gpt.Constants.GEMINI_BASE_URL
import com.apps.kunalfarmah.k_gpt.Constants.OPEN_AI_BASE_URL
import com.apps.kunalfarmah.k_gpt.network.api.GeminiApi
import com.apps.kunalfarmah.k_gpt.network.api.OpenAIApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {


    @Singleton
    @Provides
    @Named("gemini_base_url")
    fun provideGeminiBaseUrl(): String = GEMINI_BASE_URL

    @Singleton
    @Provides
    @Named("open_ai_base_url")
    fun provideOpenAIBaseUrl(): String = OPEN_AI_BASE_URL

    @Singleton
    @Provides
    fun provideOkHttpClient() = OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).readTimeout(30,
        TimeUnit.SECONDS).build()


    @Singleton
    @Provides
    @Named("gemini_retrofit")
    fun provideGeminiRetrofit(@Named("gemini_base_url") baseUrl: String, okHttpClient: OkHttpClient): Retrofit {
        val networkJson = Json { ignoreUnknownKeys = true }
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(networkJson.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    @Singleton
    @Provides
    @Named("open_ai_retrofit")
    fun provideOpenAIRetrofit(@Named("open_ai_base_url") baseUrl: String, okHttpClient: OkHttpClient): Retrofit {
        val networkJson = Json { ignoreUnknownKeys = true }
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(networkJson.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    @Singleton
    @Provides
    fun provideGeminiApi(@Named("gemini_retrofit") retrofit: Retrofit): GeminiApi {
        return retrofit.create(GeminiApi::class.java)
    }


    @Singleton
    @Provides
    fun provideOpenAIApi(@Named("open_ai_retrofit") retrofit: Retrofit): OpenAIApi {
        return retrofit.create(OpenAIApi::class.java)
    }

}