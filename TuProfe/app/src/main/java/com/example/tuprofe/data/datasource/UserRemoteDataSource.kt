package com.example.tuprofe.data.datasource

import com.example.tuprofe.data.dtos.RegisterUserDto
import com.example.tuprofe.data.dtos.UserDto

interface UserRemoteDataSource {
    suspend fun getUserById(id: String, currentUserId: String =""): UserDto?

    suspend fun registerUser(registerUserDto: RegisterUserDto, userID: String)

    suspend fun updateUser(userId: String, username: String, email: String, carrera: String)

    suspend fun updateUserPhoto(userId: String, photoUrl: String)

    suspend fun followOrUnfollowUser(currentUserId: String, targetUserId: String)

    suspend fun getFollowers(userId: String, currentUserId: String): List<UserDto>

    suspend fun getFollowing(userId: String, currentUserId: String): List<UserDto>

    suspend fun getFollowingIds(userId: String): List<String>
}
