package com.example.tuprofe.data.dtos

import com.example.tuprofe.data.Profesor

data class ProfessorDto(
    val id: String,
    val name: String,
    val department: String,
    val subjects: List<String>,
    val foto_prof: String?,
    val createdAt: String,
    val updatedAt: String
){
    constructor(): this("","","", emptyList(), null,"","")
}

fun ProfessorDto.toProfesor(): Profesor {
    return Profesor(
        profeId = this.id,
        nombreProfe = this.name,
        imageprofeUrl = this.foto_prof
    )
}
