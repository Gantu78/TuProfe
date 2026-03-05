package com.example.tuprofe.ui.Historia

import com.example.tuprofe.data.ReviewInfo

data class HistorialState(
    val userReviews: List<ReviewInfo> = emptyList(),
    val isLoading: Boolean = false
)
