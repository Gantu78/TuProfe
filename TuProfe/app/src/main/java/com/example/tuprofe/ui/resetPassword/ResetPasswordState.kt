package com.example.tuprofe.ui.resetPassword

import androidx.annotation.StringRes

data class ResetPasswordState(
    val email: String = "",
    val mostrarMensaje: Boolean = false,
    val mostrarError: Boolean = false,
    @StringRes val errorMessage: Int? = null
)
