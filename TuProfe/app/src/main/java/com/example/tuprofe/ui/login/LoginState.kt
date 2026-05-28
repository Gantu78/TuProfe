package com.example.tuprofe.ui.login

import androidx.annotation.StringRes

data class LoginState(
    val passwordVisible: Boolean = true,
    val email: String = "",
    val password: String = "",
    val navigate: Boolean = false,
    val forgotPassword: Boolean = false,
    val register: Boolean = false,
    val mostrarMensajeError: Boolean = false,
    @StringRes val errorMessage: Int? = null,
    // Social sign-in
    val isGoogleLoading: Boolean = false,
    val isGitHubLoading: Boolean = false,
)
