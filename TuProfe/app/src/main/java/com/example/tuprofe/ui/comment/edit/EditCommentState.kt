package com.example.tuprofe.ui.comment.edit

import androidx.annotation.StringRes

data class EditCommentState(
    val commentId: String = "",
    val commentText: String = "",
    val isInitialLoading: Boolean = true,
    val isLoading: Boolean = false,
    val success: Boolean = false,
    @StringRes val error: Int? = null
)
