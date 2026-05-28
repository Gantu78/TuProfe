package com.example.tuprofe.data

import java.util.Date

data class Message(
    val messageId: String,
    val senderId: String,
    val text: String,
    val sentAt: Date?,
    val read: Boolean,
    val imageUrl: String? = null
)
