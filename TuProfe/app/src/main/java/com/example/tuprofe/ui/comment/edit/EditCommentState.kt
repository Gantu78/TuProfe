package com.example.tuprofe.ui.comment.edit

data class EditCommentState(
    val commentId: String = "",
    val commentText: String = "",
    val isInitialLoading: Boolean = true,
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null
)
