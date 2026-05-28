package com.example.tuprofe.ui.profe

import androidx.annotation.StringRes
import com.example.tuprofe.data.Profesor
import com.example.tuprofe.data.ReviewInfo

data class ProfeState(
    val profesor: Profesor? = null,
    val professorReviews: List<ReviewInfo> = emptyList(),
    val averageRating: Float = 0f,
    val isLoading: Boolean = true,
    val resumenIA: String? = null,
    val isLoadingIA: Boolean = false,
    @StringRes val errorIA: Int? = null
)
