package com.example.tuprofe.ui.resetPassword

data class ResetPasswordState(
    val email: String = "",
    val mostrarMensaje: Boolean = false,
    val mostrarError: Boolean = false,
    val errorMessage: String = ""
)
