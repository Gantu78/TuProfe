package com.example.tuprofe.data.dtos

import com.example.tuprofe.data.Profesor

data class ProfessorDto(
    val id: Int,
    val name: String,
    val department: String,
    val subjects: List<String>,
    val foto_prof: String?,
    val createdAt: String,
    val updatedAt: String
)

fun ProfessorDto.toProfesor(): Profesor {
    return Profesor(
        profeId = this.id.toString(),
        nombreProfe = this.name,
        imageprofeUrl = this.foto_prof
    )
}
