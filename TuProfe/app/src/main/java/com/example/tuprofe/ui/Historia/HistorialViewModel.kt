package com.example.tuprofe.ui.Historia

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tuprofe.data.repository.ReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistorialViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(HistorialState())
    val uiState: StateFlow<HistorialState> = _uiState.asStateFlow()

    init {
        cargarHistorial()
    }

    fun cargarHistorial() {
        _uiState.update { it.copy(isLoading = true) }
        
        viewModelScope.launch {
            // Hardcoded user ID "1" as String
            val result = reviewRepository.getUserReviews("1")
            
            if (result.isSuccess) {
                _uiState.update { it.copy(
                    userReviews = result.getOrNull() ?: emptyList(),
                    isLoading = false,
                    errorMessage = null
                ) }
            } else {
                _uiState.update { it.copy(
                    isLoading = false,
                    errorMessage = result.exceptionOrNull()?.message
                ) }
            }
        }
    }

    fun deleteReview(reviewId: String) {
        val currentList = _uiState.value.userReviews
        _uiState.update { it.copy(
            userReviews = currentList.filter { it.reviewId != reviewId }
        ) }

        viewModelScope.launch {
            val result = reviewRepository.deleteReview(reviewId)
            if (result.isFailure) {
                _uiState.update { it.copy(
                    errorMessage = "Error al eliminar. Sincronizando...",
                    isLoading = true 
                ) }
                cargarHistorial()
            }
        }
    }

    fun onFilterClick(filter: String) {
        _uiState.update { it.copy(selectedFilter = filter) }
    }
}
