package com.example.tuprofe.ui.register

import androidx.annotation.StringRes

data class RegisterState(
    val email: String = "",
    val usuario: String = "",
    val carrera: String = "",
    val password1: String = "",
    val password2: String = "",
    val passwordVisible: Boolean = false,
    val mostrarMensajeError: Boolean = false,
    @StringRes val errorMessage: Int? = null,
    val mostrarMensaje: Boolean = false,
    val navigateHome: Boolean = false,
    val navigateLogin: Boolean = false,
    val isLoading: Boolean = false
)
