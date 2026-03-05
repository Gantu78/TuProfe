package com.example.tuprofe.ui.Register

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.tuprofe.R

data class RegisterState(
    val email: String = "",
    val usuario: String = "",
    val carrera: String = "",
    val password1: String = "",
    val password2: String = "",
    val passwordVisible: Boolean = false,
    val mostrarMensajeError: Boolean = false,
    val errorMessage: String = "",
    val mostrarMensaje: Boolean = false,
    val navigateHome: Boolean = false
)
