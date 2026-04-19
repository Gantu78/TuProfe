package com.example.tuprofe.ui.detalle

import com.example.tuprofe.data.ReviewInfo

data class DetalleState(
    val selectedReview: ReviewInfo? = null,
    val respuestas: List<ReviewInfo> = emptyList(),
    val currentUserId: String = "",
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val navigateToProfile: Int? = null,
    val navigateBack: Boolean = false
)
