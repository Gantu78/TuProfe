package com.example.tuprofe.data.datasource.impl.retrofit

import com.example.tuprofe.data.datasource.services.UserRetrofitService
import com.example.tuprofe.data.datasource.UserRemoteDataSource
import com.example.tuprofe.data.dtos.RegisterUserDto
import com.example.tuprofe.data.dtos.UserDto
import javax.inject.Inject

class UsuarioRemoteDataSourceImpl @Inject constructor(
    private val service: UserRetrofitService
) : UserRemoteDataSource {

    override suspend fun getUserById(id: String, currentUserId: String): UserDto {
        return try {
            service.getUserById(id, currentUserId.ifEmpty { null })
        } catch (e: retrofit2.HttpException) {
            // Usuario no registrado en Express aún (auth gestionada por Firebase)
            // Devolvemos un stub para no bloquear operaciones que solo necesitan el UID
            if (e.code() == 404) UserDto(id = id, username = "")
            else throw e
        }
    }

    override suspend fun registerUser(registerUserDto: RegisterUserDto, userID: String) {
        service.registerUser(registerUserDto)
    }

    override suspend fun updateUser(userId: String, username: String, email: String, carrera: String) {
        service.updateUser(
            userId,
            mapOf("username" to username, "email" to email, "carrera" to carrera)
        )
    }

    override suspend fun updateUserPhoto(userId: String, photoUrl: String) {
        service.updateUserPhoto(userId, mapOf("foto" to photoUrl))
    }

    override suspend fun followOrUnfollowUser(currentUserId: String, targetUserId: String) {
        service.followToggle(targetUserId, mapOf("currentUserId" to currentUserId))
    }

    override suspend fun getFollowers(userId: String, currentUserId: String): List<UserDto> {
        return service.getFollowers(userId, currentUserId.ifEmpty { null })
    }

    override suspend fun getFollowing(userId: String, currentUserId: String): List<UserDto> {
        return service.getFollowing(userId, currentUserId.ifEmpty { null })
    }

    override suspend fun getFollowingIds(userId: String): List<String> {
        return service.getFollowingIds(userId)
    }

    override suspend fun updatePrivacySettings(
        userId: String,
        perfilAnonimo: Boolean,
        perfilPublico: Boolean,
        resenasEnPerfil: Boolean
    ) { /* no-op: no soportado en Express */ }
}
