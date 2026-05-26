package com.example.tuprofe.ui.comment.detalle

import com.example.tuprofe.data.CommentInfo
import com.example.tuprofe.data.ModerationAction

data class CommentDetalleState(
    val selectedComment: CommentInfo? = null,
    val replies: List<CommentInfo> = emptyList(),
    val currentUserId: String = "",
    val isLoading: Boolean = true,
    val isLoadingReplies: Boolean = false,
    val errorMessage: String? = null,
    val showReplySheet: Boolean = false,
    val replyText: String = "",
    val isSubmittingReply: Boolean = false,
    val moderationDialog: ModerationAction? = null,
    val moderationFeedback: String? = null,
    val navigateBack: Boolean = false
)
