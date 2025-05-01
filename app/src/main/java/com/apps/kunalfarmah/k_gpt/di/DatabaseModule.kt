package com.apps.kunalfarmah.k_gpt.di

import android.content.Context
import androidx.room.Room
import com.apps.kunalfarmah.k_gpt.db.MIGRATION_1_2
import com.apps.kunalfarmah.k_gpt.db.MessageDAO
import com.apps.kunalfarmah.k_gpt.db.MessagesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): MessagesDatabase {
        return Room.databaseBuilder(
            context,
            MessagesDatabase::class.java,
            "messages_database").
            addMigrations(MIGRATION_1_2)
            .build()
    }

    @Singleton
    @Provides
    fun provideMessageDao(database: MessagesDatabase): MessageDAO {
        return database.messageDao()
    }

}