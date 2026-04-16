package com.example.tuprofe.data.datasource.impl

import com.example.tuprofe.data.datasource.Services.ReviewRetrofitService
import com.example.tuprofe.data.datasource.UsuarioRemoteDataSource
import com.example.tuprofe.data.dtos.UserDto
import javax.inject.Inject

class UsuarioRemoteDataSourceImpl @Inject constructor(
    private val service: ReviewRetrofitService
) : UsuarioRemoteDataSource {

    override suspend fun getUserById(id: String): UserDto {
        return service.getUserById(id.toInt())
    }
}
