package com.example.tuprofe.ui.Review

data class CreateReviewState(
    val reviewText: String = "",
    val professorId: String = "",
    val rating: Int = 0,
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null
)
