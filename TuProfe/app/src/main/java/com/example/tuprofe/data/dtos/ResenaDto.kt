package com.example.tuprofe.data.dtos

import com.example.tuprofe.data.Profesor
import com.example.tuprofe.data.ReviewInfo
import com.example.tuprofe.data.Usuario
import com.example.tuprofe.data.local.LocalMateria

data class ResenaDto(
    val id: Int? = null,
    val userId: Int? = null,
    val professorId: Int? = null,
    val content: String? = null,
    val time: String? = null,
    val rating: Int? = null,
    val comment: Int? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val professor: ProfessorNameDto? = null,
    val user: UserDto? = null

)

data class ProfessorNameDto(
    val name: String? = null,
    val foto: String? = null
)

data class UserDto(
    val id: Int? = null,
    val name: String? = null,
    val username: String? = null,
    val email: String? = null,
    val carrera: String? = null,
    val foto: String? = null
)

data class UsuarioDto(
    val id: Int? = null,
    val name: String? = null,
    val username: String? = null,
    val email: String? = null,
    val carrera: String? = null,
    val foto: String? = null
)

fun UserDto.toUsuario(): Usuario {
    return Usuario(
        usuarioId = id?.toString() ?: "0",
        nombreUsu = username ?: name ?: "Usuario $id",
        email = email ?: "",
        carrera = carrera ?: "",
        imageprofeUrl = foto
    )
}

fun UsuarioDto.toUsuario(): Usuario {
    return Usuario(
        usuarioId = id?.toString() ?: "0",
        nombreUsu = username ?: name ?: "Usuario $id",
        email = email ?: "",
        carrera = carrera ?: "",
        imageprofeUrl = foto
    )
}

fun ResenaDto.toReviewInfo(): ReviewInfo {
    return ReviewInfo(
        reviewId = id?.toString() ?: "",
        usuario = Usuario(
            usuarioId = userId?.toString() ?: "0",
            nombreUsu = user?.username ?: user?.name ?: "Usuario $userId",
            email = user?.email ?: "",
            carrera = user?.carrera ?: "",
            imageprofeUrl = user?.foto
        ),
        profesor = Profesor(
            profeId = professorId?.toString() ?: "0",
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
