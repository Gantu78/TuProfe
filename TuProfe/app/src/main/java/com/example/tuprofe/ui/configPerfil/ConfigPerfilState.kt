package com.example.tuprofe.ui.configPerfil

data class ConfigPerfilState(
    val email: String = "",
    val username: String = "",
    val carrera: String = "",
    val password: String = "",
    val profileImage: String? = null,
    val mostrarPassword: Boolean = false,
    val isLoading: Boolean = false,
    val navigate: Boolean = false,
    val saveSuccess: Boolean = false,
    val showDeleteDialog: Boolean = false,
    val showSaveDialog: Boolean = false,
    val errorMessagePerfil: String? = null,
    val errorMessageEliminar: String? = null
)
