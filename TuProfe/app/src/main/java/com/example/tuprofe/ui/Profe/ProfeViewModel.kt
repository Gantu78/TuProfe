package com.example.tuprofe.ui.Profe

import androidx.lifecycle.ViewModel
import com.example.tuprofe.data.local.LocalReview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.roundToInt

class ProfeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ProfeState())
    val uiState: StateFlow<ProfeState> = _uiState.asStateFlow()

    fun cargarDatosProfesor(profeId: Int) {
        _uiState.update { it.copy(isLoading = true) }
        
        // Filtramos las reseñas del profesor usando el ID
        val reviews = LocalReview.Reviews.filter { it.profesor.profeId == profeId }
        
        // Calculamos el promedio de estrellas
        val average = if (reviews.isNotEmpty()) {
            reviews.map { it.rating }.average().roundToInt()
        } else {
            0
        }

        _uiState.update { it.copy(
            professorReviews = reviews,
            averageRating = average,
            isLoading = false
        ) }
    }
}
