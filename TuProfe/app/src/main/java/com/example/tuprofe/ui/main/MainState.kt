package com.example.tuprofe.ui.main

import com.example.tuprofe.data.ReviewInfo

data class MainState(
    val reviews: List<ReviewInfo> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null,

)
