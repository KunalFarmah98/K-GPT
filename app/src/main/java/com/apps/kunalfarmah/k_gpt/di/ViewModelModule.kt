package com.apps.kunalfarmah.k_gpt.di

import com.apps.kunalfarmah.k_gpt.repository.GeminiRepository
import com.apps.kunalfarmah.k_gpt.viewmodel.GeminiViewModel
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
}