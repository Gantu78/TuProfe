package com.example.tuprofe.ui.ajustes

import com.example.tuprofe.data.repository.BlockedUser

data class AjustesState(
    val perfilAnonimo: Boolean = false,
    val perfilPublico: Boolean = true,
    val resenasEnPerfil: Boolean = true,
    val blockedUsers: List<BlockedUser> = emptyList(),
    val isLoadingBlocked: Boolean = false
)
