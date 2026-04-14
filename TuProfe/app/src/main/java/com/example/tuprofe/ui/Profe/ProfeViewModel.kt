package com.example.tuprofe.ui.Profe

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tuprofe.data.repository.ProfessorRepository
import com.example.tuprofe.data.repository.ReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class ProfeViewModel @Inject constructor(
    private val professorRepository: ProfessorRepository,
    private val reviewRepository: ReviewRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfeState())
    val uiState: StateFlow<ProfeState> = _uiState.asStateFlow()

    init {
        val idString = savedStateHandle.get<String>("profeId") ?: "0"
        cargarDatos(idString)
    }

    private fun cargarDatos(profeId: String) {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val professorResult = professorRepository.getProfessorById(profeId)
            val reviewsResult = reviewRepository.getReviews()

            if (professorResult.isSuccess && reviewsResult.isSuccess) {
                val professor = professorResult.getOrNull()
                val allReviews = reviewsResult.getOrNull() ?: emptyList()
                
                // Filtrar reseñas para este profesor
                val filteredReviews = allReviews.filter { it.profesor.profeId == profeId.toString() }

                val average = if (filteredReviews.isNotEmpty()) {
                    filteredReviews.map { it.rating }.average().roundToInt()
                } else 0

                _uiState.update {
                    it.copy(
                        profesor = professor,
                        professorReviews = filteredReviews,
                        averageRating = average,
                        isLoading = false
                    )
                }
            } else {
                _uiState.update { 
                    it.copy(
                        isLoading = false
                    ) 
                }
            }
        }
    }
}
