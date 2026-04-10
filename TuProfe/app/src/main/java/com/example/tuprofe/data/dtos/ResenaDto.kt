package com.example.tuprofe.data.dtos

import com.example.tuprofe.data.ReviewInfo
import com.example.tuprofe.data.local.LocalMateria
import com.example.tuprofe.data.local.LocalProfesor

data class ResenaDto(
    val id: String? = null,
    val userId: String? = null,
    val professorId: String? = null,
    val materiaId: String? = null,
    val likes: Int? = null,
    val content: String? = null,
    val time: String? = null,
    val rating: Int? = null,
    val comment: Int? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)

fun ResenaDto.toReviewInfo(): ReviewInfo {

    return ReviewInfo(
        reviewId = id ?: "",
        profesor = LocalProfesor.profesores.find { it.profeId == professorId }
            ?: LocalProfesor.profesores.first(),
        username = userId ?: "Usuario",
        materia = LocalMateria.materias.find { it.materiaId == materiaId }
            ?: LocalMateria.materias.first(),
        likes = likes ?: 0,
        content = content ?: "",
        time = time ?: "",
        rating = rating ?: 0,
        comments = comment ?: 0
    )
}
