package com.example.tuprofe.data.datasource.services

import com.example.tuprofe.data.dtos.UserDto
import retrofit2.http.GET
import retrofit2.http.Path


interface UserRetrofitService {



    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: Int): UserDto
}
