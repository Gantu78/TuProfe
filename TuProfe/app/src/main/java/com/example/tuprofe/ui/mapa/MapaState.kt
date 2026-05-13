package com.example.tuprofe.ui.mapa

data class ReviewMapMarker(
    val reviewId: String,
    val profesorNombre: String,
    val rating: Int,
    val latitude: Double,
    val longitude: Double
)

data class MapaState(
    val isLoading: Boolean = true,
    val markers: List<ReviewMapMarker> = emptyList(),
    val selectedMarker: ReviewMapMarker? = null,
    val error: String? = null
)
