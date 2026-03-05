package com.example.tuprofe.ui.Profe

import com.example.tuprofe.data.ReviewInfo

data class ProfeState(
    val professorReviews: List<ReviewInfo> = emptyList(),
    val averageRating: Int = 0,
    val isLoading: Boolean = false
)
