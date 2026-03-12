package com.example.tuprofe.ui.Login

data class LoginState(
    val passwordVisible: Boolean = true,
    val email: String = "",
    val password: String = "",
    val navigate: Boolean = false,
    val forgotPassword: Boolean = false,
    val register: Boolean = false,
    val mostrarMensajeError: Boolean = false,
    val errorMessage: String = ""
)
