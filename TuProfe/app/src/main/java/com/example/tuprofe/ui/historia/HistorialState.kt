package com.example.tuprofe.ui.historia

import com.example.tuprofe.data.ReviewInfo

data class HistorialState(
    val userReviews: List<ReviewInfo> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val selectedFilter: String? = null,
    val navigateToReviewId: Int? = null
)
