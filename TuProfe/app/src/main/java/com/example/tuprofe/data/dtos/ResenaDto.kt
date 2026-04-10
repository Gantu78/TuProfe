package com.example.tuprofe.data.dtos

import com.example.tuprofe.data.Profesor
import com.example.tuprofe.data.ReviewInfo
import com.example.tuprofe.data.local.LocalMateria

data class ResenaDto(
    val id: String? = null,
    val userId: String? = null,
    val professorId: String? = null,
    val content: String? = null,
    val time: String? = null,
    val rating: Int? = null,
    val comment: Int? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val professor: ProfessorNameDto? = null
)

data class ProfessorNameDto(
    val name: String? = null,
    val foto: String? = null
)

fun ResenaDto.toReviewInfo(): ReviewInfo {
    return ReviewInfo(
        reviewId = id ?: "",
        profesor = Profesor(
            profeId = professorId ?: "0",
            nombreProfe = professor?.name ?: "Profesor #$professorId",
            imageprofeUrl = professor?.foto
        ),
        username = "Usuario $userId",
        materia = LocalMateria.materias.first(),
        likes = 0,
        content = content ?: "",
        time = time ?: "",
        rating = rating ?: 0,
        comments = comment ?: 0
    )
}
