package com.example.tuprofe.ui.login

data class LoginState(
    val passwordVisible: Boolean = true,
    val email: String = "",
    val password: String = "",
    val navigate: Boolean = false,
    val forgotPassword: Boolean = false,
    val register: Boolean = false,
    val mostrarMensajeError: Boolean = false,
    val errorMessage: String = "",
    // Social sign-in
    val isGoogleLoading: Boolean = false,
    val isGitHubLoading: Boolean = false,
)
