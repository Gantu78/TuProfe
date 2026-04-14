package com.example.tuprofe.data.datasource.impl

import com.example.tuprofe.data.datasource.ProfessorRemoteDataSource
import com.example.tuprofe.data.datasource.Services.ReviewRetrofitService
import com.example.tuprofe.data.dtos.ProfessorDto
import javax.inject.Inject

class ProfessorRemoteDataSourceImpl @Inject constructor(
    private val service: ReviewRetrofitService
) : ProfessorRemoteDataSource {
    override suspend fun getAllProfessors(): List<ProfessorDto> {
        return service.getAllProfessors()
    }

    override suspend fun getProfessorById(id: String): ProfessorDto {
        return service.getProfessorById(id.toInt())
    }
}
