package com.example.tuprofe.data.repository

import com.example.tuprofe.data.Usuario
import com.example.tuprofe.data.datasource.impl.firestore.UserFirestoreDataSourceImpl
import com.example.tuprofe.data.datasource.impl.retrofit.UsuarioRemoteDataSourceImpl
import com.example.tuprofe.data.dtos.RegisterUserDto
import com.example.tuprofe.data.dtos.toUsuario
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userRemoteDataSource: UserFirestoreDataSourceImpl
) {
    suspend fun getUserById(userId: String): Result<Usuario> {
        return try {
            val userDto = userRemoteDataSource.getUserById(userId)
            Result.success(userDto.toUsuario())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun registerUser(username: String, carrera: String, userId: String): Result<Unit> {
        return try {
            val registerUserDto = RegisterUserDto(
                id= userId,
                username = username,
                carrera = carrera
            )
            userRemoteDataSource.registerUser(registerUserDto, userId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
