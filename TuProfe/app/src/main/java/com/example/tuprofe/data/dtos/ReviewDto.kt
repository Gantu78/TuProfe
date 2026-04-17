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
    // El ID real de navegación es el userId (UID de Firebase) del autor.
    // Si el objeto 'user' anidado no trae ID, usamos el 'userId' del nivel superior del DTO.
    val authorId = userId ?: user?.id ?: "0"
    
    val usuarioModel = if (user != null) {
        user.toUsuario().copy(usuarioId = authorId)
    } else {
        Usuario(
            usuarioId = authorId,
            nombreUsu = "Usuario $authorId",
            email = "",
            carrera = "",
            imageprofeUrl = null
        )
    }

    return ReviewInfo(
        reviewId = id ?: "",
        usuario = usuarioModel,
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
