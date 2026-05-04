package com.example.tuprofe.ui.historia

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tuprofe.data.repository.AuthRepository
import com.example.tuprofe.data.repository.CommentRepository
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
    private val reviewRepository: ReviewRepository,
    private val commentRepository: CommentRepository,
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(HistorialState())
    val uiState: StateFlow<HistorialState> = _uiState.asStateFlow()

    init {
        cargarHistorial()
    }

    fun cargarHistorial() {
        _uiState.update { it.copy(isLoading = true) }
        val userId = authRepository.currentUser?.uid ?: return

        viewModelScope.launch {
            val reviewsResult = reviewRepository.getUserReviews(userId)
            val commentsResult = commentRepository.getUserComments(userId)

            _uiState.update {
                it.copy(
                    userReviews = reviewsResult.getOrNull() ?: it.userReviews,
                    userComments = commentsResult.getOrNull() ?: it.userComments,
                    isLoading = false,
                    errorMessage = reviewsResult.exceptionOrNull()?.message
                )
            }
        }
    }

    fun deleteReview(reviewId: String) {
        _uiState.update { it.copy(userReviews = it.userReviews.filter { r -> r.reviewId != reviewId }) }
        viewModelScope.launch {
            val result = reviewRepository.deleteReview(reviewId)
            if (result.isFailure) {
                _uiState.update { it.copy(errorMessage = "Error al eliminar. Sincronizando...", isLoading = true) }
                cargarHistorial()
            }
        }
    }

    fun deleteComment(commentId: String) {
        _uiState.update { it.copy(userComments = it.userComments.filter { c -> c.commentId != commentId }) }
        viewModelScope.launch {
            val result = commentRepository.deleteComment(commentId)
            if (result.isFailure) {
                _uiState.update { it.copy(errorMessage = "Error al eliminar. Sincronizando...", isLoading = true) }
                cargarHistorial()
            }
        }
    }

    fun setFilter(filter: HistorialFilter) {
        _uiState.update { it.copy(selectedFilter = filter) }
    }

    fun onFilterClick(filter: String) {
        val next = when (_uiState.value.selectedFilter) {
            HistorialFilter.TODO -> HistorialFilter.RESENAS
            HistorialFilter.RESENAS -> HistorialFilter.COMENTARIOS
            HistorialFilter.COMENTARIOS -> HistorialFilter.TODO
        }
        _uiState.update { it.copy(selectedFilter = next) }
    }
}
