package com.example.tuprofe.data

import androidx.annotation.DrawableRes

data class ReviewInfo(
    val reviewId: Int,
    val profesor: Profesor,
    val username: String,
    val materia: Materia,
    val likes: Int,
    val content: String,
    val time: String,
    val rating: Int,
    val comments: Int
)