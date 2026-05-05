package com.example.tuprofe.ui.comment.detalle

import com.example.tuprofe.data.CommentInfo

data class CommentDetalleState(
    val selectedComment: CommentInfo? = null,
    val replies: List<CommentInfo> = emptyList(),
    val currentUserId: String = "",
    val isLoading: Boolean = true,
    val isLoadingReplies: Boolean = false,
    val errorMessage: String? = null,
    val showReplySheet: Boolean = false,
    val replyText: String = "",
    val isSubmittingReply: Boolean = false
)
