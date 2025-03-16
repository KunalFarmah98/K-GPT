package com.apps.kunalfarmah.k_gpt.repository

import com.apps.kunalfarmah.k_gpt.data.Message
import com.apps.kunalfarmah.k_gpt.db.MessageDAO
import com.apps.kunalfarmah.k_gpt.db.MessageEntity
import com.apps.kunalfarmah.k_gpt.util.Util.getDate
import javax.inject.Inject

open class MessagesRepository @Inject constructor(private val messageDAO: MessageDAO) {

    suspend fun getAllMessages(platform: String): List<Message> {
        return messageDAO.getAllMessages(platform).mapIndexed { index, messageEntity ->
            Message(
                id = messageEntity.id,
                time = messageEntity.time,
                isUser = messageEntity.isUser,
                text = messageEntity.text,
                platform = messageEntity.platform,
                firstMessageInDay = (index == 0 || (getDate(messageEntity.time) != getDate(messageDAO.getAllMessages(platform)[index - 1].time))),
                fromHistory = true
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