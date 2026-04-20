package com.example.tuprofe.data.dtos

import com.example.tuprofe.data.Profesor
import com.example.tuprofe.data.Materia
import com.example.tuprofe.data.ReviewInfo
import com.example.tuprofe.data.Usuario
import java.text.SimpleDateFormat
import java.util.Locale

private fun formatReviewDate(raw: String?): String {
    if (raw.isNullOrEmpty()) return ""
    return try {
        val input = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        val output = SimpleDateFormat("d 'de' MMMM 'de' yyyy", Locale("es"))
        output.format(input.parse(raw)!!)
    } catch (e: Exception) {
        raw
    }
}

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
    val materia: String? = null,
    val professor: ProfessorNameDto? = null,
    val user: UserDto? = null,
    val likesCount: Int,
    var liked: Boolean = false
){
    constructor(): this("", "", "", "", "", 0, 0, "", "", null, null, null, 0)
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
            imageprofeUrl = null,
            followingCount = 0,
            followersCount = 0,
            followed = false
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
        materia = Materia(materiaId = "", nombreMateria = materia ?: ""),
        content = content ?: "",
        rating = rating ?: 0,
        time = formatReviewDate(time),
        likes = likesCount,
        commentsCount = comment ?: 0,
        liked = liked,
        editado = !updatedAt.isNullOrEmpty()
    )
}
