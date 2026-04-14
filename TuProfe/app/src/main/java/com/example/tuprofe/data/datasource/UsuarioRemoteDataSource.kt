package com.example.tuprofe.data.datasource

import com.example.tuprofe.data.dtos.UsuarioDto

interface UsuarioRemoteDataSource {


    suspend fun getUserById(id: String): UsuarioDto
}