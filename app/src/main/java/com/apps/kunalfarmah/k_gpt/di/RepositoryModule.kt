package com.apps.kunalfarmah.k_gpt.di

import com.apps.kunalfarmah.k_gpt.db.MessageDAO
import com.apps.kunalfarmah.k_gpt.network.api.GeminiApi
import com.apps.kunalfarmah.k_gpt.network.api.OpenAIApi
import com.apps.kunalfarmah.k_gpt.repository.GeminiRepository
import com.apps.kunalfarmah.k_gpt.repository.OpenAIRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.merge
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {
    @Singleton
    @Provides
    fun provideGeminiRepository(geminiApi: GeminiApi, messageDAO: MessageDAO)  = GeminiRepository(geminiApi, messageDAO)

    @Singleton
    @Provides
    fun provideOpenAIRepository(openAIApi: OpenAIApi, messageDAO: MessageDAO)  = OpenAIRepository(openAIApi, messageDAO)
}