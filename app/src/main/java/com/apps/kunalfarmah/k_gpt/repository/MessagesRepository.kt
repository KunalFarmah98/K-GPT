package com.apps.kunalfarmah.k_gpt.repository

import com.apps.kunalfarmah.k_gpt.data.Message
import com.apps.kunalfarmah.k_gpt.db.MessageDAO
import com.apps.kunalfarmah.k_gpt.db.MessageEntity
import javax.inject.Inject

open class MessagesRepository @Inject constructor(private val messageDAO: MessageDAO) {

    suspend fun getAllMessages(platform: String): List<Message>{
        return messageDAO.getAllMessages(platform).map {
            Message(
                id = it.id,
                time = it.time,
                isUser = it.isUser,
                text = it.text,
                platform = it.platform
            )
        }
    }

    suspend fun insertMessage(message: Message){
        messageDAO.insertMessage(MessageEntity(
            id = message.id,
            time = message.time,
            isUser = message.isUser,
            text = message.text,
            platform = message.platform
        ))
    }

    suspend fun deleteOlderMessages(currTime: Long){
        messageDAO.deleteOlderMessages(currTime)
    }

    suspend fun deleteAllMessages(){
        messageDAO.deleteAllMessages()
    }

    suspend fun deleteAllMessages(platform: String){
        messageDAO.deleteAllMessages(platform)
    }

}