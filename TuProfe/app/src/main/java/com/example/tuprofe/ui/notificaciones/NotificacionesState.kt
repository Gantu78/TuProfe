package com.example.tuprofe.ui.notificaciones

data class NotificacionesState(
    val likesEnabled: Boolean = true,
    val comentariosEnabled: Boolean = true,
    val seguidoresEnabled: Boolean = true,
    val permissionGranted: Boolean = false
)
