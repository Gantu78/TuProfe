package com.example.tuprofe.ui.ConfigPerfil

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

data class ConfigPerfilState(
    val email: String = "c.jimenez@javeriana.edu.co",
    val username: String = "Gantu970",
    val carrera: String = "Ingenieria Mecatrónica",
    val showDeleteDialog: Boolean = false,
    val navigate: Boolean = false,
    val showChangePasswordDialog: Boolean = false,
    val showSaveDialog: Boolean = false
)
