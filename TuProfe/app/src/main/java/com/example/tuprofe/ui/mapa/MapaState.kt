package com.example.tuprofe.ui.mapa

import com.example.tuprofe.data.ReviewMapMarker

data class MapaState(
    val isLoading: Boolean = true,
    val markers: List<ReviewMapMarker> = emptyList(),
    val allMarkers: List<ReviewMapMarker> = emptyList(),
    val selectedMarker: ReviewMapMarker? = null,
    val selectedGroup: List<ReviewMapMarker>? = null,
    val error: String? = null,
    val showReviewList: Boolean = false,
    val navigateToMarker: ReviewMapMarker? = null,
    val showFilterPanel: Boolean = false,
    val filterStars: Set<Int> = emptySet(),
    val filterProfesores: Set<String> = emptySet(),
    val filterMaterias: Set<String> = emptySet()
) {
    val hasActiveFilters: Boolean
        get() = filterStars.isNotEmpty() || filterProfesores.isNotEmpty() || filterMaterias.isNotEmpty()
}
