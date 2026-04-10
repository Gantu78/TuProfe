package com.example.tuprofe.data.dtos

data class CreateReviewDto(
    val userId: Int? = null,
    val professorId: Int? = null,
    val content: String? = null,
    val rating: Int? = null,
    val time: String? = null
)
