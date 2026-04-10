package com.example.tuprofe.ui.Profe

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tuprofe.data.datasource.ResenaRemoteDataSource
import com.example.tuprofe.data.datasource.Services.ReviewRetrofitService
import com.example.tuprofe.data.dtos.toProfesor
import com.example.tuprofe.data.dtos.toReviewInfo
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
    private val service: ReviewRetrofitService,
    private val resenaRemoteDataSource: ResenaRemoteDataSource,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfeState())
    val uiState: StateFlow<ProfeState> = _uiState.asStateFlow()

    init {
        val idString = savedStateHandle.get<String>("profeId") ?: "0"
        val id = idString.toIntOrNull() ?: 0
        cargarDatosProfesor(id)
    }

    private fun cargarDatosProfesor(profeId: Int) {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            try {
                // 1. Obtener datos del profesor reales
                val professorDto = service.getProfessorById(profeId)
                val profesorReal = professorDto.toProfesor()

                // 2. Obtener reseñas y filtrar
                val allReviewsDto = resenaRemoteDataSource.getAllReviews()
                val reviews = allReviewsDto
                    .filter { it.professorId == profeId.toString() || it.professorId == profeId.toDouble().toString() }
                    .map { 
                        // Seteamos el profesor real a cada reseña para que tenga su nombre y foto
                        it.toReviewInfo().copy(profesor = profesorReal) 
                    }

                val average = if (reviews.isNotEmpty()) {
                    reviews.map { it.rating }.average().roundToInt()
                } else 0

                _uiState.update {
                    it.copy(
                        profesor = profesorReal,
                        professorReviews = reviews,
                        averageRating = average,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}
