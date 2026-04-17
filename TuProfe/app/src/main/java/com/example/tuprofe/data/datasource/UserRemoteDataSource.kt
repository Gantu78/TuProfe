package com.example.tuprofe.data.datasource

import com.example.tuprofe.data.dtos.RegisterUserDto
import com.example.tuprofe.data.dtos.UserDto

interface UserRemoteDataSource {
    suspend fun getUserById(id: String): UserDto

    suspend fun registerUser(registerUserDto: RegisterUserDto, userID: String): Unit

    suspend fun updateUser(userId: String, username: String, email: String, carrera: String): Unit

    suspend fun updateUserPhoto(userId: String, photoUrl: String): Unit
}
