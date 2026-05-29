package com.example.tuprofe.data.datasource

import com.example.tuprofe.data.AppNotification
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
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
                    if (title.isBlank()) return@mapNotNull null  // descarta entradas vacías del servidor
                    val ts = when (val raw = d["timestamp"]) {
                        is Timestamp -> raw.toDate().time
                        is Long -> raw
                        else -> 0L
                    }
                    AppNotification(
                        id = doc.id,
                        type = d["type"] as? String ?: "",
                        entityId = d["entityId"] as? String ?: "",
                        title = title,
                        body = d["body"] as? String ?: "",
                        senderId = d["senderId"] as? String ?: "",
                        senderName = d["senderName"] as? String ?: "",
                        senderImageUrl = d["senderImageUrl"] as? String ?: "",
                        timestamp = ts,
                        isRead = d["isRead"] as? Boolean ?: false
                    )
                }?.sortedByDescending { it.timestamp } ?: emptyList()
                trySend(list)
            }
        awaitClose { listener.remove() }
    }

    fun listenUnreadCount(userId: String): Flow<Int> = callbackFlow {
        val listener = userNotifs(userId)
            .whereEqualTo("isRead", false)
            .addSnapshotListener { snapshot, _ ->
                val count = snapshot?.documents?.count { doc ->
                    (doc.getString("title") ?: "").isNotBlank()
                } ?: 0
                trySend(count)
            }
        awaitClose { listener.remove() }
    }

    suspend fun saveNotification(userId: String, notification: AppNotification) {
        userNotifs(userId).add(
            mapOf(
                "type" to notification.type,
                "entityId" to notification.entityId,
                "title" to notification.title,
                "body" to notification.body,
                "senderId" to notification.senderId,
                "senderName" to notification.senderName,
                "senderImageUrl" to notification.senderImageUrl,
                "timestamp" to FieldValue.serverTimestamp(),
                "isRead" to false
            )
        ).await()
    }

    suspend fun markRead(userId: String, notifId: String) {
        userNotifs(userId).document(notifId).update("isRead", true).await()
    }

    suspend fun markAllRead(userId: String) {
        val unread = userNotifs(userId).whereEqualTo("isRead", false).get().await()
        if (unread.documents.isEmpty()) return
        val batch = db.batch()
        unread.documents.forEach { batch.update(it.reference, "isRead", true) }
        batch.commit().await()
    }
}