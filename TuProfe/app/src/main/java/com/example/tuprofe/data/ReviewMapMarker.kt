package com.example.tuprofe.data

data class ReviewMapMarker(
    val reviewId: String,
    val profesorNombre: String,
    val rating: Int,
    val latitude: Double,
    val longitude: Double,
    val materia: String = "",
    val profesorFotoUrl: String? = null,
    val authorUserId: String = ""
)
