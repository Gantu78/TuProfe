package com.example.tuprofe.data

data class ReviewInfo(
    val reviewId: String,
    val usuario: Usuario,
    val profesor: Profesor,
    val materia: Materia,
    val content: String,
    val rating: Int,
    val time: String,
    val likes: Int = 0,
    val commentsCount: Int = 0
)
