package com.example.tuprofe.ui.main

import com.example.tuprofe.data.ReviewInfo

data class MainState(
    val reviews: List<ReviewInfo> = emptyList(),
    val followingReviews: List<ReviewInfo> = emptyList(),
    val selectedTab: Int = 0,
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)
