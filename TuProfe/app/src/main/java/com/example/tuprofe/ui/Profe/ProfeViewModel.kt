package com.example.tuprofe.ui.Profe

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.tuprofe.data.local.LocalProfesor
import com.example.tuprofe.data.local.LocalReview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.roundToInt

class ProfeViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfeState())
    val uiState: StateFlow<ProfeState> = _uiState.asStateFlow()

    init {
        val id = savedStateHandle.get<Int>("profeId") ?: 0
        cargarDatosProfesor(id)
    }

    private fun cargarDatosProfesor(profeId: Int) {
        _uiState.update { it.copy(isLoading = true) }
        
        val profesor = LocalProfesor.profesores.find { it.profeId == profeId }
        val reviews = LocalReview.Reviews.filter { it.profesor.profeId == profeId }
        val average = if (reviews.isNotEmpty()) {
            reviews.map { it.rating }.average().roundToInt()
        } else {
            0
        }

        _uiState.update { it.copy(
            profesor = profesor,
            professorReviews = reviews,
            averageRating = average,
            isLoading = false
        ) }
    }

}
