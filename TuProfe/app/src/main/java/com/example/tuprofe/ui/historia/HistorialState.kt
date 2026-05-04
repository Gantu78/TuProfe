package com.example.tuprofe.ui.historia

import com.example.tuprofe.data.CommentInfo
import com.example.tuprofe.data.ReviewInfo

enum class HistorialFilter { TODO, RESENAS, COMENTARIOS }

data class HistorialState(
    val userReviews: List<ReviewInfo> = emptyList(),
    val userComments: List<CommentInfo> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val selectedFilter: HistorialFilter = HistorialFilter.TODO,
    val navigateToReviewId: Int? = null
)
