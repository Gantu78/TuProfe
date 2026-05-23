package com.example.tuprofe.data

data class Usuario(
    val usuarioId: String,
    val nombreUsu: String,
    val email: String,
    val carrera: String,
    val imageprofeUrl: String?,
    val followingCount: Int,
    val followersCount: Int,
    var followed: Boolean = false,
    val perfilAnonimo: Boolean = false,
    val perfilPublico: Boolean = true,
    val resenasEnPerfil: Boolean = true
)
