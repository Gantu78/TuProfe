package com.example.tuprofe.data.datasource.services

import com.example.tuprofe.data.dtos.RegisterUserDto
import com.example.tuprofe.data.dtos.UserDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface UserRetrofitService {

    @GET("users/{id}")
    suspend fun getUserById(
        @Path("id") id: String,
        @Query("currentUserId") currentUserId: String? = null
    ): UserDto

    /** Crea/actualiza el perfil tras el registro con Firebase Auth */
    @POST("users/register")
    suspend fun registerUser(@Body dto: RegisterUserDto): UserDto

    /** Actualiza username, email y/o carrera */
    @PUT("users/{id}")
    suspend fun updateUser(
        @Path("id") id: String,
        @Body body: Map<String, String>
    ): UserDto

    /** Actualiza la foto de perfil. Body: { "foto": "url" } */
    @PUT("users/{id}/photo")
    suspend fun updateUserPhoto(
        @Path("id") id: String,
        @Body body: Map<String, String>
    ): UserDto

    /** Sigue o deja de seguir al usuario {id}. Body: { "currentUserId": "..." } */
    @POST("users/{id}/follow-toggle")
    suspend fun followToggle(
        @Path("id") id: String,
        @Body body: Map<String, String>
    ): Map<String, Any>

    @GET("users/{id}/followers")
    suspend fun getFollowers(
        @Path("id") id: String,
        @Query("currentUserId") currentUserId: String? = null
    ): List<UserDto>

    @GET("users/{id}/following")
    suspend fun getFollowing(
        @Path("id") id: String,
        @Query("currentUserId") currentUserId: String? = null
    ): List<UserDto>

    @GET("users/{id}/following/ids")
    suspend fun getFollowingIds(@Path("id") id: String): List<String>
}
