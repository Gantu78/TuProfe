package com.example.tuprofe.data.repository

import android.util.Log
import com.example.tuprofe.data.Profesor
import com.example.tuprofe.data.datasource.ProfessorRemoteDataSource
import com.example.tuprofe.data.datasource.impl.firestore.ProfessorFirestoreDataSourceImpl
import com.example.tuprofe.data.datasource.impl.retrofit.ProfessorRemoteDataSourceImpl
import com.example.tuprofe.data.dtos.toProfesor
import javax.inject.Inject

class ProfessorRepository @Inject constructor(
    private val professorRemoteDataSource: ProfessorFirestoreDataSourceImpl
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
            Log.i("DATOS","$id")
            val professor = professorRemoteDataSource.getProfessorById(id).toProfesor()
            Result.success(professor)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
