package com.example.tuprofe.data.repository

import com.example.tuprofe.data.Usuario
import com.example.tuprofe.data.datasource.ProfessorRemoteDataSource
import com.example.tuprofe.data.datasource.Services.ReviewRetrofitService
import com.example.tuprofe.data.datasource.UsuarioRemoteDataSource
import com.example.tuprofe.data.datasource.impl.firestore.UserFirestoreDataSourceImpl
import com.example.tuprofe.data.dtos.RegisterUserDto
import com.example.tuprofe.data.dtos.toUsuario
import javax.inject.Inject
import kotlin.math.log

class UserRepository @Inject constructor(
    private val userRemoteDataSource: UsuarioRemoteDataSource
) {
    suspend fun getUserById(userId: String): Result<Usuario> {
        return try {
            val userDto = userRemoteDataSource.getUserById(userId)
            Result.success(userDto.toUsuario())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun registerUser(email: String, username: String, carrera: String, userId: String): Result<Unit> {
        return try {
            val registerUserDto = RegisterUserDto(
                email = email,
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
