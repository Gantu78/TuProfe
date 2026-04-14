package com.example.tuprofe.data.datasource

import com.example.tuprofe.data.dtos.ProfessorDto

interface ProfessorRemoteDataSource {
    suspend fun getAllProfessors(): List<ProfessorDto>
    suspend fun getProfessorById(id: String): ProfessorDto
}
