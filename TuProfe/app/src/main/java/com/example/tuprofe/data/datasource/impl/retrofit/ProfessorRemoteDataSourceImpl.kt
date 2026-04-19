package com.example.tuprofe.data.datasource.impl.retrofit

import com.example.tuprofe.data.datasource.ProfessorRemoteDataSource
import com.example.tuprofe.data.datasource.services.ProfesorRetrofitService
import com.example.tuprofe.data.dtos.ProfessorDto
import javax.inject.Inject

class ProfessorRemoteDataSourceImpl @Inject constructor(
    private val service: ProfesorRetrofitService
) : ProfessorRemoteDataSource {
    override suspend fun getAllProfessors(): List<ProfessorDto> {
        return service.getAllProfessors()
    }

    override suspend fun getProfessorById(id: String): ProfessorDto {
        return service.getProfessorById(id.toInt())
    }
}
