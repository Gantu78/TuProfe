package com.example.tuprofe.data.dtos

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class ChatDto(
    @DocumentId val chatId: String = "",
    val participantIds: List<String> = emptyList(),
    val lastMessage: String = "",
    @ServerTimestamp val lastMessageAt: Date? = null,
    val unreadCounts: Map<String, Long> = emptyMap()
) {
    constructor() : this("", emptyList(), "", null, emptyMap())
}

data class MessageDto(
    @DocumentId val messageId: String = "",
    val senderId: String = "",
    val text: String = "",
    @ServerTimestamp val sentAt: Date? = null,
    val read: Boolean = false
) {
    constructor() : this("", "", "", null, false)
}
