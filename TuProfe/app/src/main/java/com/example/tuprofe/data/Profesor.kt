package com.example.tuprofe.data

import androidx.annotation.DrawableRes

data class Profesor(
    val profeId: Int,
    val nombreProfe: String,
    @DrawableRes val imageprofeId: Int,
)
