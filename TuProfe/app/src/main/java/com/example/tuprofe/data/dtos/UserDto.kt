package com.example.tuprofe.data.dtos

import com.example.tuprofe.data.Usuario

data class UserDto(
    val id: String? = null,
    val name: String? = null,
    val username: String? = null,
    val email: String? = null,
    val carrera: String? = null,
    val foto: String? = null,
    val followingCount: Int = 0,
    val followersCount: Int = 0,
    var followed: Boolean = false
){
    constructor(): this("","","","","","")
}



fun UserDto.toUsuario(): Usuario {
    return Usuario(
        usuarioId = id?: "0",
        nombreUsu = username ?: name ?: "Usuario $id",
        email = email ?: "",
        carrera = carrera ?: "",
        imageprofeUrl = foto,
        followingCount = followingCount,
        followersCount = followersCount,
        followed = followed
    )
}
