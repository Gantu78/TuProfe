package com.example.tuprofe.ui.chat

import com.example.tuprofe.data.ChatInfo

data class ChatListState(
    val chats: List<ChatInfo> = emptyList(),
    val isLoading: Boolean = true,
    val currentUserId: String = ""
)
