package com.example.tuprofe.ui.Review

data class CreateReviewState(
    val reviewText: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)
