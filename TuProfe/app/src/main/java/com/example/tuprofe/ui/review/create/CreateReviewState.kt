package com.example.tuprofe.ui.review.create

import com.example.tuprofe.data.Profesor

data class CreateReviewState(
    val reviewText: String = "",
    val professorQuery: String = "",
    val professors: List<Profesor> = emptyList(),
    val filteredProfessors: List<Profesor> = emptyList(),
    val selectedProfessor: Profesor? = null,
    val selectedMateria: String = "",
    val isMateriaDropdownExpanded: Boolean = false,
    val rating: Int = 0,
    val isLoading: Boolean = false,
    val isSearchingProfessors: Boolean = false,
    val success: Boolean = false,
    val error: String? = null,
    val isDropdownExpanded: Boolean = false
)