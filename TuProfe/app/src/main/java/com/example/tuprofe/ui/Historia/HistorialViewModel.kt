package com.example.tuprofe.ui.Historia

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tuprofe.data.datasource.ResenaRemoteDataSource
import com.example.tuprofe.data.dtos.toReviewInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistorialViewModel @Inject constructor(
    private val resenaRemoteDataSource: ResenaRemoteDataSource
): ViewModel() {
    private val _uiState = MutableStateFlow(HistorialState())
    val uiState: StateFlow<HistorialState> = _uiState.asStateFlow()

    init {
        cargarHistorial()
    }

    fun cargarHistorial() {
        _uiState.update { it.copy(isLoading = true) }
        
        viewModelScope.launch {
            try {
                // Obtenemos las reseñas directamente del usuario 1 desde el backend
                val userReviewsDto = resenaRemoteDataSource.getUserReviews(1)
                
                val userReviews = userReviewsDto.map { it.toReviewInfo() }

                _uiState.update { it.copy(
                    userReviews = userReviews,
                    isLoading = false
                ) }
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    isLoading = false,
                    errorMessage = e.message
                ) }
            }
        }
    }

    fun deleteReview(reviewId: String) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                resenaRemoteDataSource.deleteReview(reviewId)
                // Esperar un poco o asegurar que el servidor procesó
                cargarHistorial()
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    isLoading = false,
                    errorMessage = "Error al eliminar: ${e.message}" 
                ) }
            }
        }
    }

    fun onFilterClick(filter: String) {
        _uiState.update { it.copy(selectedFilter = filter) }
    }
}
