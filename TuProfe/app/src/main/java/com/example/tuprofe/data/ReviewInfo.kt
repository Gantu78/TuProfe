package com.example.tuprofe.data

import androidx.annotation.DrawableRes

data class ReviewInfo(
    @DrawableRes val imageId: Int,
    val name: String,
    val materia: String,
    val likes: Int,
    val content: String,
    val time: String,
    val rating: Int,
    val comments: Int
)