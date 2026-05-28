package com.example.tuprofe.data

import java.util.Date

data class ChatInfo(
    val chatId: String,
    val otherUserId: String,
    val otherUserName: String,
    val otherUserImage: String?,
    val lastMessage: String,
    val lastMessageAt: Date?,
    val unreadCount: Int,
    val lastMessageSenderId: String = ""
)
