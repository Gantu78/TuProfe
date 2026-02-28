package com.example.tuprofe.data

import androidx.annotation.DrawableRes

data class ReviewInfo(
    val reviewId: Int,
    @DrawableRes val imageId: Int,
    val userName: String,
    val profeName: String,
    val materia: String,
    val likes: Int,
    val content: String,
    val time: String,
    val rating: Int,
    val comments: Int
)