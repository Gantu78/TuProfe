package com.example.tuprofe.ui.Config

data class ConfigState(
    val username: String = "Gantu20",
    val email: String = "",
    val carrera: String = "Ingenieria Industrial",
    val isLoading: Boolean = false,
    val profileImageUrl: String? = null,
    val errorMessage: String? = null,
)
