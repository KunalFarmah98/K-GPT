package com.apps.kunalfarmah.k_gpt.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MessageDAO {

    @Query("SELECT * FROM messages WHERE platform = :platform ORDER BY time")
    suspend fun getAllMessages(platform: String): List<MessageEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageEntity)

    @Query("DELETE FROM messages WHERE time < :currTime")
    suspend fun deleteOlderMessages(currTime: Long)

    @Query("DELETE FROM messages where platform = :platform")
    suspend fun deleteAllMessages(platform: String)

    @Query("DELETE FROM messages")
    suspend fun deleteAllMessages()

}