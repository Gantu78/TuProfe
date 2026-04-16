package com.example.tuprofe.data.datasource

import com.example.tuprofe.data.dtos.RegisterUserDto
import com.example.tuprofe.data.dtos.UserDto

interface UsuarioRemoteDataSource {
    suspend fun getUserById(id: String): UserDto

    suspend fun  registerUser(registerUserDto: RegisterUserDto, userID: String): Unit
}
