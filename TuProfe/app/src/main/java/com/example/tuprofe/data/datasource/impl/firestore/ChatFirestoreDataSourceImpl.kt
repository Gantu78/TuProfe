package com.example.tuprofe.data.datasource.impl.firestore

import com.example.tuprofe.data.ChatInfo
import com.example.tuprofe.data.Message
import com.example.tuprofe.data.datasource.ChatRemoteDataSource
import com.example.tuprofe.data.dtos.ChatDto
import com.example.tuprofe.data.dtos.MessageDto
import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ChatFirestoreDataSourceImpl @Inject constructor(
    private val db: FirebaseFirestore
) : ChatRemoteDataSource {

    private val chats = db.collection("chats")

    override suspend fun getOrCreateChat(chatId: String, participantIds: List<String>) {
        val doc = chats.document(chatId).get().await()
        if (!doc.exists()) {
            val data = mapOf(
                "participantIds" to participantIds,
                "lastMessage" to "",
                "lastMessageAt" to FieldValue.serverTimestamp(),
                "unreadCounts" to participantIds.associateWith { 0L }
            )
            chats.document(chatId).set(data).await()
        }
    }

    override fun listenMessages(chatId: String): Flow<List<Message>> = callbackFlow {
        val listener = chats.document(chatId)
            .collection("messages")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("ChatFirestore", "listenMessages error: ${error.message}")
                    return@addSnapshotListener
                }
                val messages = snapshot?.documents?.mapNotNull { doc ->
                    val data = doc.data ?: return@mapNotNull null
                    val sentAt = (data["sentAt"] as? com.google.firebase.Timestamp)?.toDate()
                    Message(
                        messageId = doc.id,
                        senderId = data["senderId"] as? String ?: "",
                        text = data["text"] as? String ?: "",
                        sentAt = sentAt,
                        read = data["read"] as? Boolean ?: false,
                        imageUrl = data["imageUrl"] as? String
                    )
                } ?: emptyList()
                // Sort client-side ascending by sentAt
                trySend(messages.sortedBy { it.sentAt })
            }
        awaitClose { listener.remove() }
    }

    override fun listenChats(userId: String): Flow<List<ChatInfo>> = callbackFlow {
        // No orderBy to avoid requiring a composite Firestore index — sorted client-side
        val listener = chats
            .whereArrayContains("participantIds", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("ChatFirestore", "listenChats error: ${error.message}")
                    return@addSnapshotListener
                }
                val list = snapshot?.documents?.mapNotNull { doc ->
                    val data = doc.data ?: return@mapNotNull null
                    @Suppress("UNCHECKED_CAST")
                    val participantIds = data["participantIds"] as? List<String> ?: return@mapNotNull null
                    val otherUserId = participantIds.firstOrNull { it != userId } ?: return@mapNotNull null
                    val unreadCountsRaw = data["unreadCounts"] as? Map<*, *> ?: emptyMap<String, Any>()
                    val unreadCount = (unreadCountsRaw[userId] as? Long ?: 0L).toInt()
                    val lastMessageAt = (data["lastMessageAt"] as? com.google.firebase.Timestamp)?.toDate()
                    ChatInfo(
                        chatId = doc.id,
                        otherUserId = otherUserId,
                        otherUserName = "",
                        otherUserImage = null,
                        lastMessage = data["lastMessage"] as? String ?: "",
                        lastMessageAt = lastMessageAt,
                        unreadCount = unreadCount,
                        lastMessageSenderId = data["lastMessageSenderId"] as? String ?: ""
                    )
                } ?: emptyList()
                // Sort client-side by lastMessageAt descending
                trySend(list.sortedByDescending { it.lastMessageAt })
            }
        awaitClose { listener.remove() }
    }

    override suspend fun sendMessage(chatId: String, senderId: String, text: String, imageUrl: String?) {
        val messageData = mutableMapOf<String, Any>(
            "senderId" to senderId,
            "text" to text,
            "sentAt" to FieldValue.serverTimestamp(),
            "read" to false
        )
        if (imageUrl != null) messageData["imageUrl"] = imageUrl
        chats.document(chatId).collection("messages").add(messageData).await()
        val lastMsg = if (text.isNotBlank()) text else "📷 Imagen"
        chats.document(chatId).update(
            mapOf(
                "lastMessage" to lastMsg,
                "lastMessageAt" to FieldValue.serverTimestamp(),
                "lastMessageSenderId" to senderId
            )
        ).await()
    }

    override suspend fun markRead(chatId: String, userId: String) {
        chats.document(chatId).update("unreadCounts.$userId", 0L).await()
    }

    override suspend fun getOtherUserInfo(userId: String): Pair<String, String?> {
        val doc = db.collection("users").document(userId).get().await()
        val name = doc.getString("username") ?: doc.getString("name") ?: "Usuario"
        val photo = doc.getString("foto")
        return Pair(name, photo)
    }
}
