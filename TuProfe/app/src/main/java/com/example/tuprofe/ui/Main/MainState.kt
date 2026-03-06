package com.example.tuprofe.ui.Main

import com.example.tuprofe.data.ReviewInfo

data class MainState(
    val reviews: List<ReviewInfo> = emptyList(),
    val isLoading: Boolean = true,
    val navigateToReviewId: Int? = null,
    val navigateToProfileId: Int? = null
)
