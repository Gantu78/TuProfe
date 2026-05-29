package com.example.tuprofe.ui.review.create

import android.net.Uri
import androidx.annotation.StringRes
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
    @StringRes val error: Int? = null,
    val isDropdownExpanded: Boolean = false,
    val includeLocation: Boolean = false,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val selectedImageUris: List<Uri> = emptyList(),
    val isUploadingImages: Boolean = false
) {
    val canSubmit: Boolean
        get() = selectedProfessor != null &&
                selectedMateria.isNotBlank() &&
                rating > 0 &&
                reviewText.isNotBlank() &&
                !isLoading
}