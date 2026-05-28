package com.example.tuprofe.ui.chat

import android.net.Uri
import com.example.tuprofe.data.Message

data class ChatState(
    val messages: List<Message> = emptyList(),
    val otherUserName: String = "",
    val otherUserImage: String? = null,
    val currentUserId: String = "",
    val isLoading: Boolean = true,
    val isSending: Boolean = false,
    val pendingImageUri: Uri? = null
)
