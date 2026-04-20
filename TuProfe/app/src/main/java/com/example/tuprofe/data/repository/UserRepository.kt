package com.example.tuprofe.data.repository

import com.example.tuprofe.data.Usuario
import com.example.tuprofe.data.datasource.StorageRemoteDataSource
import com.example.tuprofe.data.datasource.UserRemoteDataSource
import com.example.tuprofe.data.dtos.RegisterUserDto
import com.example.tuprofe.data.dtos.toUsuario
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val storageRemoteDataSource: StorageRemoteDataSource
) {
    suspend fun getUserById(userId: String, CurrentUserId:String =""): Result<Usuario> {
        return try {
            val userDto = userRemoteDataSource.getUserById(userId, CurrentUserId)
            val usuario = userDto.toUsuario()
            val withPhoto = if (usuario.imageprofeUrl.isNullOrEmpty()) {
                val photoUrl = storageRemoteDataSource.getProfileImageUrl(userId)
                usuario.copy(imageprofeUrl = photoUrl)
            } else {
                usuario
            }
            Result.success(withPhoto)
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

    suspend fun updateUser(userId: String, username: String, email: String, carrera: String): Result<Unit> {
        return try {
            userRemoteDataSource.updateUser(userId, username, email, carrera)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateUserPhoto(userId: String, photoUrl: String): Result<Unit> {
        return try {
            userRemoteDataSource.updateUserPhoto(userId, photoUrl)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun followOrUnfollow(currentUserId: String, targetUserId: String): Result<Unit>{
        return try{
            userRemoteDataSource.followOrUnfollowUser(currentUserId, targetUserId)
            return Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getFollowers(userId: String, currentUserId: String): Result<List<Usuario>> {
        return try {
            val dtos = userRemoteDataSource.getFollowers(userId, currentUserId)
            Result.success(dtos.map { it.toUsuario() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getFollowing(userId: String, currentUserId: String): Result<List<Usuario>> {
        return try {
            val dtos = userRemoteDataSource.getFollowing(userId, currentUserId)
            Result.success(dtos.map { it.toUsuario() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getFollowingIds(userId: String): Result<List<String>> {
        return try {
            Result.success(userRemoteDataSource.getFollowingIds(userId))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
