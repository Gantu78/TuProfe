package com.example.tuprofe.ui.Review

import com.example.tuprofe.data.Profesor

data class EditReviewState(
    val reviewId: String = "",
    val reviewText: String = "",
    val professorName: String = "", // Fixed name for display since we usually don't change the professor on edit
    val rating: Int = 0,
    val isLoading: Boolean = false,
    val isInitialLoading: Boolean = true,
    val success: Boolean = false,
    val error: String? = null
)
