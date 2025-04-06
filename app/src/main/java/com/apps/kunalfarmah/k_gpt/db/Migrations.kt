package com.apps.kunalfarmah.k_gpt.db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE messages ADD COLUMN mimeType TEXT")
        database.execSQL("ALTER TABLE messages ADD COLUMN imageData TEXT")
        database.execSQL("ALTER TABLE messages ADD COLUMN isImage INTEGER NOT NULL DEFAULT 0") // Use INTEGER for Boolean
    }
}