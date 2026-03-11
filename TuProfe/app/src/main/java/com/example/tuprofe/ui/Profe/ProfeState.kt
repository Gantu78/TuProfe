package com.example.tuprofe.ui.Profe

import com.example.tuprofe.data.Profesor
import com.example.tuprofe.data.ReviewInfo

data class ProfeState(
    val profesor: Profesor? = null,
    val professorReviews: List<ReviewInfo> = emptyList(),
    val averageRating: Int = 0,
    val isLoading: Boolean = true
)
