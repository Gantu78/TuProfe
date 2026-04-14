package com.example.tuprofe.data.repository

import com.example.tuprofe.data.Usuario
import com.example.tuprofe.data.datasource.ProfessorRemoteDataSource
import com.example.tuprofe.data.datasource.Services.ReviewRetrofitService
import com.example.tuprofe.data.datasource.UsuarioRemoteDataSource
import com.example.tuprofe.data.dtos.toUsuario
import javax.inject.Inject

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
}
