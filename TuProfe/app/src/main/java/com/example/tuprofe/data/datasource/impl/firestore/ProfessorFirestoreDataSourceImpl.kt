package com.example.tuprofe.data.datasource.impl.firestore

import com.example.tuprofe.data.datasource.ProfessorRemoteDataSource
import com.example.tuprofe.data.dtos.ProfessorDto
import com.example.tuprofe.data.dtos.ReviewDto
import com.example.tuprofe.data.dtos.UserDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProfessorFirestoreDataSourceImpl @Inject constructor(
    private val db: FirebaseFirestore
) : ProfessorRemoteDataSource {

    override suspend fun getAllProfessors(): List<ProfessorDto> {
        return db.collection("professors").get().await().documents.map { doc ->
            val dto = doc.toObject(ProfessorDto::class.java) ?: ProfessorDto()
            dto.copy(id = doc.id) // ← Mapear el document ID explícitamente
        }
    }

    override suspend fun getProfessorById(id: String): ProfessorDto {
        val respuesta = db.collection("professors").document(id).get().await()
        val dto = respuesta.toObject(ProfessorDto::class.java)
            ?: throw Exception("No se pudo obtener el profesor con id: $id")
        return dto.copy(id = respuesta.id) // ← También aquí
    }
}