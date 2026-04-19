package com.example.tuprofe.ui.config

data class ConfigState(
    val username: String = "",
    val email: String = "",
    val carrera: String = "",
    val isLoading: Boolean = false,
    val profileImageUrl: String? = null,
    val errorMessage: String? = null,
)
