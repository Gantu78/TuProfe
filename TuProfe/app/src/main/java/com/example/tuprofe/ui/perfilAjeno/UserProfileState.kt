package com.example.tuprofe.ui.perfilAjeno

import com.example.tuprofe.data.ReviewInfo
import com.example.tuprofe.data.Usuario

data class UserProfileState(
    val user: Usuario? = null,
    val userReviews: List<ReviewInfo> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
