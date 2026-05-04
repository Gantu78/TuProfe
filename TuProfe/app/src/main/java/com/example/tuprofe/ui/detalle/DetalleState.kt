package com.example.tuprofe.ui.detalle

import com.example.tuprofe.data.CommentInfo
import com.example.tuprofe.data.ReviewInfo

data class DetalleState(
    val selectedReview: ReviewInfo? = null,
    val comments: List<CommentInfo> = emptyList(),
    val currentUserId: String = "",
    val isLoading: Boolean = true,
    val isLoadingComments: Boolean = false,
    val errorMessage: String? = null,
    val navigateToProfile: Int? = null,
    val navigateBack: Boolean = false,
    val showCommentSheet: Boolean = false,
    val commentText: String = "",
    val isSubmittingComment: Boolean = false
) {
    // Keep for backwards compat with preview
    val respuestas: List<CommentInfo> get() = comments
}
