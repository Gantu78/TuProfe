package com.example.tuprofe.data

data class ReviewInfo(
    val reviewId: String,
    val usuario: Usuario,
    val profesor: Profesor,
    val materia: Materia,
    val content: String,
    val rating: Int,
    val time: String,
    val likes: Int,
    val commentsCount: Int = 0,
    val liked: Boolean = false,
    val editado: Boolean = false,
    val imageUrls: List<String> = emptyList(),
    val latitude: Double? = null,
    val longitude: Double? = null
)
