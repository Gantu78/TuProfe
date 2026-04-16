package com.example.tuprofe.data.dtos

import com.example.tuprofe.data.Profesor
import com.example.tuprofe.data.ReviewInfo
import com.example.tuprofe.data.Usuario
import com.example.tuprofe.data.local.LocalMateria

data class ReviewDto(
    val id: String? = null,
    val userId: String? = null,
    val professorId: String? = null,
    val content: String? = null,
    val time: String? = null,
    val rating: Int? = null,
    val comment: Int? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val professor: ProfessorNameDto? = null,
    val user: UserDto? = null
){
    constructor(): this("", "", "", "", "", 0, 0, "", "", null, null)
}

data class ProfessorNameDto(
    val name: String? = null,
    val foto: String? = null
)
 
fun ReviewDto.toReviewInfo(): ReviewInfo {
    return ReviewInfo(
        reviewId = id ?: "",
        usuario = user?.toUsuario() ?: Usuario(
            usuarioId = userId ?: "0",
            nombreUsu = "Usuario $userId",
            email = "",
            carrera = "",
            imageprofeUrl = null
        ),
        profesor = Profesor(
            profeId = professorId ?: "0",
            nombreProfe = professor?.name ?: "Profesor #$professorId",
            imageprofeUrl = professor?.foto
        ),
        materia = LocalMateria.materias.first(),
        content = content ?: "",
        rating = rating ?: 0,
        time = time ?: "",
        likes = 0,
        commentsCount = comment ?: 0
    )
}
