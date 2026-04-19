package com.example.tuprofe.data.repository

import android.util.Log
import com.example.tuprofe.data.Profesor
import com.example.tuprofe.data.datasource.ProfessorRemoteDataSource
import com.example.tuprofe.data.dtos.toProfesor
import javax.inject.Inject

class ProfessorRepository @Inject constructor(
    private val professorRemoteDataSource: ProfessorRemoteDataSource
) {
    suspend fun getProfessors(): Result<List<Profesor>> {
        return try {
            val professors = professorRemoteDataSource.getAllProfessors().map { it.toProfesor() }
            Result.success(professors)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getProfessorById(id: String): Result<Profesor> {
        return try {
            val professor = professorRemoteDataSource.getProfessorById(id).toProfesor()
            Result.success(professor)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
