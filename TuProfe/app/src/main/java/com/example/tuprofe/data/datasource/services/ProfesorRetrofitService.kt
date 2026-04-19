package com.example.tuprofe.data.datasource.services

import com.example.tuprofe.data.dtos.ProfessorDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ProfesorRetrofitService {
    @GET("professors/{id}")
    suspend fun getProfessorById(@Path("id") id: Int): ProfessorDto

    @GET("professors")
    suspend fun getAllProfessors(): List<ProfessorDto>

}
