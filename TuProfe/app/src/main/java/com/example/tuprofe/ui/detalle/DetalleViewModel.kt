package com.example.tuprofe.ui.detalle

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
class DetalleViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository,
    private val commentRepository: CommentRepository,
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
            cargarComentarios(reviewId)
        }
    }

    private fun cargarComentarios(reviewId: String) {
        _uiState.update { it.copy(isLoadingComments = true) }
        viewModelScope.launch {
            val result = commentRepository.getCommentsByReviewId(reviewId, _uiState.value.currentUserId)
            result
                .onSuccess { comments ->
                    _uiState.update { it.copy(comments = comments, isLoadingComments = false) }
                }
                .onFailure {
                    _uiState.update { it.copy(isLoadingComments = false) }
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

    fun sendOrDeleteCommentLike(commentId: String) {
        val userId = _uiState.value.currentUserId
        viewModelScope.launch {
            val result = commentRepository.sendOrDeleteCommentLike(commentId, userId)
            if (result.isSuccess) {
                _uiState.update { state ->
                    state.copy(
                        comments = state.comments.map { c ->
                            if (c.commentId == commentId) {
                                c.copy(
                                    liked = !c.liked,
                                    likes = if (c.liked) c.likes - 1 else c.likes + 1
                                )
                            } else c
                        }
                    )
                }
            }
        }
    }

    fun openCommentSheet() {
        _uiState.update { it.copy(showCommentSheet = true, commentText = "") }
    }

    fun closeCommentSheet() {
        _uiState.update { it.copy(showCommentSheet = false, commentText = "") }
    }

    fun onCommentTextChange(text: String) {
        _uiState.update { it.copy(commentText = text) }
    }

    fun submitComment(reviewId: String) {
        val state = _uiState.value
        if (state.commentText.isBlank() || state.isSubmittingComment) return

        _uiState.update { it.copy(isSubmittingComment = true) }
        viewModelScope.launch {
            val result = commentRepository.createComment(
                reviewId = reviewId,
                parentCommentId = null,
                userId = state.currentUserId,
                content = state.commentText.trim()
            )
            result.onSuccess {
                _uiState.update { s ->
                    s.copy(
                        isSubmittingComment = false,
                        showCommentSheet = false,
                        commentText = "",
                        selectedReview = s.selectedReview?.copy(
                            commentsCount = s.selectedReview.commentsCount + 1
                        )
                    )
                }
                cargarComentarios(reviewId)
            }.onFailure {
                _uiState.update { it.copy(isSubmittingComment = false) }
            }
        }
    }
}
