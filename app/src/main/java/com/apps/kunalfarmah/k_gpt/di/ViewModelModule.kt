package com.apps.kunalfarmah.k_gpt.di

import com.apps.kunalfarmah.k_gpt.repository.GeminiRepository
import com.apps.kunalfarmah.k_gpt.repository.OpenAIRepository
import com.apps.kunalfarmah.k_gpt.viewmodel.GeminiViewModel
import com.apps.kunalfarmah.k_gpt.viewmodel.OpenAIViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ViewModelModule {

    @Singleton
    @Provides
    fun provideGeminiViewModel(geminiRepository: GeminiRepository) = GeminiViewModel(geminiRepository)

    @Singleton
    @Provides
    fun provideOpenAIViewModel(openAIRepository: OpenAIRepository) = OpenAIViewModel(openAIRepository)
}