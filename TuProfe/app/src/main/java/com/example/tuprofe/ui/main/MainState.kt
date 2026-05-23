package com.example.tuprofe.ui.main

import com.example.tuprofe.data.ReviewInfo

enum class SortOrder(val label: String) {
    RECIENTES("Más recientes"),
    MEJOR_CALIFICADAS("Mejor calificadas"),
    MAS_GUSTADAS("Más gustadas")
}

data class MainState(
    val reviews: List<ReviewInfo> = emptyList(),
    val followingReviews: List<ReviewInfo> = emptyList(),
    val selectedTab: Int = 0,
    val sortOrder: SortOrder = SortOrder.RECIENTES,
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)
