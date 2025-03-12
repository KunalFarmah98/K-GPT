package com.apps.kunalfarmah.k_gpt.di

import com.apps.kunalfarmah.k_gpt.network.model.api.GeminiApi
import com.apps.kunalfarmah.k_gpt.repository.GeminiRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {
    @Singleton
    @Provides
    fun provideGeminiRepository(geminiApi: GeminiApi)  = GeminiRepository(geminiApi)
}