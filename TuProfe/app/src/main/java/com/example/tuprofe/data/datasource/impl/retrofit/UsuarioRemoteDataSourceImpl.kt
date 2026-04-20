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
        return service.getUserById(id.toInt())
    }


    override suspend fun registerUser(registerUserDto: RegisterUserDto, userID: String) {
        TODO("Not yet implemented")
    }

    override suspend fun updateUser(
        userId: String,
        username: String,
        email: String,
        carrera: String
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun updateUserPhoto(userId: String, photoUrl: String) {
        TODO("Not yet implemented")
    }

    override suspend fun followOrUnfollowUser(currentUserId: String, targetUserId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getFollowers(userId: String, currentUserId: String): List<UserDto> {
        TODO("Not yet implemented")
    }

    override suspend fun getFollowing(userId: String, currentUserId: String): List<UserDto> {
        TODO("Not yet implemented")
    }

    override suspend fun getFollowingIds(userId: String): List<String> {
        TODO("Not yet implemented")
    }
}
