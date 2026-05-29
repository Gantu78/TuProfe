package com.example.tuprofe.data.datasource

import com.example.tuprofe.data.ChatInfo
import com.example.tuprofe.data.Message
import kotlinx.coroutines.flow.Flow

interface ChatRemoteDataSource {
    suspend fun getOrCreateChat(chatId: String, participantIds: List<String>)
    fun listenMessages(chatId: String): Flow<List<Message>>
    fun listenChats(userId: String): Flow<List<ChatInfo>>
    suspend fun sendMessage(chatId: String, senderId: String, recipientId: String, text: String, imageUrl: String? = null)
    suspend fun markRead(chatId: String, userId: String)
    suspend fun getOtherUserInfo(userId: String): Pair<String, String?>
}
