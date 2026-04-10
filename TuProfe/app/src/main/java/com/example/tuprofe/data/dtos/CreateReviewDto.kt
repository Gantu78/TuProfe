package com.example.tuprofe.data.dtos

data class CreateReviewDto(
    val userId: String? = null,
    val professorId: String? = null,
    val materiaId: String? = null,
    val content: String? = null,
)
