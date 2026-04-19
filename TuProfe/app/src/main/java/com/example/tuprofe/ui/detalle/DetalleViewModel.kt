package com.example.tuprofe.ui.detalle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tuprofe.data.repository.AuthRepository
import com.example.tuprofe.data.repository.ReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetalleViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetalleState())
    val uiState: StateFlow<DetalleState> = _uiState.asStateFlow()

    init {
        _uiState.update { it.copy(currentUserId = authRepository.currentUser?.uid ?: "") }
    }

    fun cargarDetalle(reviewId: String) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = reviewRepository.getReviewById(reviewId, _uiState.value.currentUserId)
            result
                .onSuccess { review ->
                    _uiState.update { it.copy(selectedReview = review, isLoading = false) }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = error.message) }
                }
        }
    }

    fun sendOrDeleteReviewLike(reviewId: String, userId: String) {
        viewModelScope.launch {
            val result = reviewRepository.sendOrDeleteReviewLike(reviewId, userId)
            if (result.isSuccess) {
                _uiState.update {
                    val review = it.selectedReview ?: return@update it
                    it.copy(
                        selectedReview = review.copy(
                            liked = !review.liked,
                            likes = if (review.liked) review.likes - 1 else review.likes + 1
                        )
                    )
                }
            }
        }
    }
}
