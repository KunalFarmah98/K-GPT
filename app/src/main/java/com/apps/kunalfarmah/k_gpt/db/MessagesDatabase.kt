package com.apps.kunalfarmah.k_gpt.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [MessageEntity::class], version = 1, exportSchema = false)
abstract class MessagesDatabase : RoomDatabase() {
    abstract fun messageDao(): MessageDAO
}