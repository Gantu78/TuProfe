package com.example.tuprofe.data.dtos

import com.example.tuprofe.data.Usuario

data class UserDto(
    val id: Int? = null,
    val name: String? = null,
    val username: String? = null,
    val email: String? = null,
    val carrera: String? = null,
    val foto: String? = null
)

fun UserDto.toUsuario(): Usuario {
    return Usuario(
        usuarioId = id?.toString() ?: "0",
        nombreUsu = username ?: name ?: "Usuario $id",
        email = email ?: "",
        carrera = carrera ?: "",
        imageprofeUrl = foto
    )
}
