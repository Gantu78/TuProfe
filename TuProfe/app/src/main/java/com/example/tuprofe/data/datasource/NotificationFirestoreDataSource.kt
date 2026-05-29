package com.example.tuprofe.data.datasource

import com.example.tuprofe.data.AppNotification
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class NotificationFirestoreDataSource @Inject constructor(
    private val db: FirebaseFirestore
) {

    private fun userNotifs(userId: String) =
        db.collection("users").document(userId).collection("notifications")

    fun listenNotifications(userId: String): Flow<List<AppNotification>> = callbackFlow {
        val listener = userNotifs(userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) return@addSnapshotListener
                val list = snapshot?.documents?.mapNotNull { doc ->
                    val d = doc.data ?: return@mapNotNull null
                    val title = d["title"] as? String ?: ""
                    if (title.isBlank()) return@mapNotNull null
                    val type = d["type"] as? String ?: ""
                    val reviewId = d["reviewId"] as? String ?: ""
                    val commentId = d["commentId"] as? String ?: ""
                    val fromUserId = d["fromUserId"] as? String ?: ""
                    val entityId = entityId(type, reviewId, commentId, fromUserId)
                    val ts = when (val raw = d["createdAt"]) {
                        is Timestamp -> raw.toDate().time
                        is Long -> raw
                        else -> 0L
                    }
                    // CF uses "read"; client-saved docs used "isRead" — support both
                    val isRead = d["read"] as? Boolean ?: d["isRead"] as? Boolean ?: false
                    AppNotification(
                        id = doc.id,
                        type = type,
                        entityId = entityId,
                        title = title,
                        body = d["body"] as? String ?: "",
                        senderId = fromUserId,
                        senderName = d["fromUsername"] as? String ?: "",
                        senderImageUrl = d["senderImageUrl"] as? String ?: "",
                        timestamp = ts,
                        isRead = isRead
                    )
                }?.sortedByDescending { it.timestamp } ?: emptyList()
                trySend(list)
            }
        awaitClose { listener.remove() }
    }

    fun listenUnreadCount(userId: String): Flow<Int> = callbackFlow {
        val listener = userNotifs(userId)
            .addSnapshotListener { snapshot, _ ->
                val count = snapshot?.documents?.count { doc ->
                    val d = doc.data ?: return@count false
                    val title = d["title"] as? String ?: ""
                    val read = d["read"] as? Boolean ?: d["isRead"] as? Boolean ?: false
                    title.isNotBlank() && !read
                } ?: 0
                trySend(count)
            }
        awaitClose { listener.remove() }
    }

    suspend fun markRead(userId: String, notifId: String) {
        userNotifs(userId).document(notifId).update("read", true).await()
    }

    suspend fun markAllRead(userId: String) {
        val unread = userNotifs(userId)
            .whereEqualTo("read", false)
            .get().await()
        if (unread.documents.isEmpty()) return
        val batch = db.batch()
        unread.documents.forEach { batch.update(it.reference, "read", true) }
        batch.commit().await()
    }
}

private fun entityId(type: String, reviewId: String, commentId: String, fromUserId: String): String =
    when (type) {
        "like", "reviewDeleted" -> reviewId
        "comment", "reply" -> commentId.ifBlank { reviewId }
        "follow" -> fromUserId
        else -> reviewId.ifBlank { commentId }.ifBlank { fromUserId }
    }
