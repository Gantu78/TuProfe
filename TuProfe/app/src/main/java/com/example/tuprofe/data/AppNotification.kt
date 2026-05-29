package com.example.tuprofe.data

data class AppNotification(
    val id: String = "",
    val type: String = "",        // "like", "comment", "follow", "review"
    val entityId: String = "",    // reviewId, commentId, or userId depending on type
    val title: String = "",
    val body: String = "",
    val senderId: String = "",
    val senderName: String = "",
    val senderImageUrl: String = "",
    val timestamp: Long = 0L,
    val isRead: Boolean = false
)