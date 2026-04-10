package com.example.tuprofe.ui.Review

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
class CreateReviewViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateReviewState())
    val uiState: StateFlow<CreateReviewState> = _uiState.asStateFlow()

    fun onReviewTextChange(newText: String) {
        _uiState.update { it.copy(reviewText = newText) }
    }

    fun onProfessorIdChange(id: String) {
        _uiState.update { it.copy(professorId = id) }
    }

    fun onRatingChange(newRating: Int) {
        _uiState.update { it.copy(rating = newRating) }
    }

    fun createReview() {
        val currentState = _uiState.value
        
        // Hardcoded userId to 1 (Int) as requested
        val userId = 1
        val professorId = currentState.professorId.toIntOrNull() ?: 0

        if (professorId == 0) {
            _uiState.update { it.copy(error = "ID de profesor inválido") }
            return
        }

        if (currentState.rating < 1 || currentState.rating > 5) {
            _uiState.update { it.copy(error = "La calificación debe estar entre 1 y 5") }
            return
        }

        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            val result = reviewRepository.createReview(
                userId = userId,
                professorId = professorId,
                content = currentState.reviewText,
                rating = currentState.rating
            )

            if (result.isSuccess) {
                _uiState.update { it.copy(isLoading = false, success = true) }
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = result.exceptionOrNull()?.message ?: "Error desconocido"
                    )
                }
            }
        }
    }

    fun resetSuccess() {
        _uiState.update { it.copy(success = false) }
    }
}
