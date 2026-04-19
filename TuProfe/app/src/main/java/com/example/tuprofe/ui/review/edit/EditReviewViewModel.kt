package com.example.tuprofe.ui.review.edit

import androidx.lifecycle.SavedStateHandle
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
class EditReviewViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditReviewState())
    val uiState: StateFlow<EditReviewState> = _uiState.asStateFlow()

    init {
        val reviewId = savedStateHandle.get<String>("reviewId") ?: ""
        if (reviewId.isNotEmpty()) {
            cargarReview(reviewId)
        }
    }

    private fun cargarReview(reviewId: String) {
        _uiState.update { it.copy(reviewId = reviewId, isInitialLoading = true) }
        viewModelScope.launch {
            val result = reviewRepository.getReviewById(reviewId)
            if (result.isSuccess) {
                val review = result.getOrNull()
                if (review != null) {
                    _uiState.update {
                        it.copy(
                            reviewText = review.content,
                            rating = review.rating,
                            professorName = review.profesor.nombreProfe,
                            isInitialLoading = false
                        )
                    }
                } else {
                    _uiState.update { it.copy(error = "Reseña no encontrada", isInitialLoading = false) }
                }
            } else {
                _uiState.update { it.copy(error = "Error al cargar datos", isInitialLoading = false) }
            }
        }
    }

    fun onReviewTextChange(newText: String) {
        _uiState.update { it.copy(reviewText = newText) }
    }

    fun onRatingChange(newRating: Int) {
        _uiState.update { it.copy(rating = newRating) }
    }

    fun updateReview() {
        val currentState = _uiState.value
        if (currentState.reviewText.isBlank()) {
            _uiState.update { it.copy(error = "El contenido no puede estar vacío") }
            return
        }

        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            val result = reviewRepository.updateReview(
                reviewId = currentState.reviewId,
                content = currentState.reviewText,
                rating = currentState.rating
            )

            if (result.isSuccess) {
                _uiState.update { it.copy(isLoading = false, success = true) }
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = result.exceptionOrNull()?.message ?: "Error al actualizar"
                    )
                }
            }
        }
    }
}