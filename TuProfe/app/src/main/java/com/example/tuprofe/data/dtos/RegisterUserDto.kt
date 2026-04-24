package com.example.tuprofe.data.dtos

data class RegisterUserDto(
    val id: String = "",      // ← Agregar el ID
    val username: String = "",
    val carrera: String = "",
    val FCMToken: String
)
