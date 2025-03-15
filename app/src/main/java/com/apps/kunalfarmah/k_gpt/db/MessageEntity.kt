package com.apps.kunalfarmah.k_gpt.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val time: Long = Date().time,
    val isUser: Boolean = true,
    val text: String = "",
    val platform: String = ""
)