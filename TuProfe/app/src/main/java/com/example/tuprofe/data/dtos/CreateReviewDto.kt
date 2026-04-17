package com.example.tuprofe.data.dtos

data class CreateReviewUserDto(
    val username: String? = null
)

data class CreateReviewProfessorDto(
    val name: String? = null,
    val department: String? = null,
    val foto: String? = null
)
data class CreateReviewDto(
    var userId: String? = null,
    val professorId: String? = null,
    val content: String? = null,
    val rating: Int? = null,
    val time: String? = null,

    var user: CreateReviewUserDto? = null,
    val professor: CreateReviewProfessorDto? = null
)


