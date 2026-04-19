package com.example.tuprofe.ui.detalle

import com.example.tuprofe.data.ReviewInfo

data class DetalleState(
    val selectedReview: ReviewInfo? = null,
    val respuestas: List<ReviewInfo> = emptyList(),
    val isLoading: Boolean = true,
    val navigateToProfile: Int? = null,
    val navigateBack: Boolean = false
)
