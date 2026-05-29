package com.example.tuprofe.data.repository

import android.net.Uri
import com.example.tuprofe.data.ChatInfo
import com.example.tuprofe.data.Message
import com.example.tuprofe.data.datasource.ChatRemoteDataSource
import com.example.tuprofe.data.datasource.StorageRemoteDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChatRepository @Inject constructor(
    private val chatDataSource: ChatRemoteDataSource,
    private val storageDataSource: StorageRemoteDataSource
) {
    fun chatIdFor(uid1: String, uid2: String): String =
        listOf(uid1, uid2).sorted().joinToString("_")

    suspend fun getOrCreateChat(chatId: String, participantIds: List<String>): Result<Unit> =
        runCatching { chatDataSource.getOrCreateChat(chatId, participantIds) }

    fun listenMessages(chatId: String): Flow<List<Message>> =
        chatDataSource.listenMessages(chatId)

    fun listenChats(userId: String): Flow<List<ChatInfo>> =
        chatDataSource.listenChats(userId)

    suspend fun sendMessage(chatId: String, senderId: String, recipientId: String, text: String, imageUrl: String? = null): Result<Unit> =
        runCatching { chatDataSource.sendMessage(chatId, senderId, recipientId, text, imageUrl) }

    suspend fun uploadChatImage(userId: String, uri: Uri): Result<String> =
        runCatching { storageDataSource.uploadChatImage(userId, uri) }

    suspend fun markRead(chatId: String, userId: String): Result<Unit> =
        runCatching { chatDataSource.markRead(chatId, userId) }

    suspend fun getOtherUserInfo(userId: String): Result<Pair<String, String?>> =
        runCatching { chatDataSource.getOtherUserInfo(userId) }
}
