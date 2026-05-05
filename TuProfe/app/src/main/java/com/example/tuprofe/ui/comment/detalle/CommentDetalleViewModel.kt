package com.example.tuprofe.ui.comment.detalle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tuprofe.data.repository.AuthRepository
import com.example.tuprofe.data.repository.CommentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommentDetalleViewModel @Inject constructor(
    private val commentRepository: CommentRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CommentDetalleState())
    val uiState: StateFlow<CommentDetalleState> = _uiState.asStateFlow()

    init {
        _uiState.update { it.copy(currentUserId = authRepository.currentUser?.uid ?: "") }
    }

    fun cargarComentario(commentId: String) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = commentRepository.getCommentById(commentId, _uiState.value.currentUserId)
            result
                .onSuccess { comment ->
                    _uiState.update { it.copy(selectedComment = comment, isLoading = false) }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = error.message) }
                }
            cargarRespuestas(commentId)
        }
    }

    private fun cargarRespuestas(commentId: String) {
        _uiState.update { it.copy(isLoadingReplies = true) }
        viewModelScope.launch {
            val result = commentRepository.getRepliesByCommentId(commentId, _uiState.value.currentUserId)
            result
                .onSuccess { replies ->
                    _uiState.update { it.copy(replies = replies, isLoadingReplies = false) }
                }
                .onFailure {
                    _uiState.update { it.copy(isLoadingReplies = false) }
                }
        }
    }

    fun sendOrDeleteCommentLike(commentId: String) {
        val userId = _uiState.value.currentUserId
        viewModelScope.launch {
            val result = commentRepository.sendOrDeleteCommentLike(commentId, userId)
            if (result.isSuccess) {
                _uiState.update { state ->
                    val selected = state.selectedComment
                    if (selected != null && selected.commentId == commentId) {
                        state.copy(
                            selectedComment = selected.copy(
                                liked = !selected.liked,
                                likes = if (selected.liked) selected.likes - 1 else selected.likes + 1
                            )
                        )
                    } else {
                        state.copy(
                            replies = state.replies.map { c ->
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
    }

    fun openReplySheet() {
        _uiState.update { it.copy(showReplySheet = true, replyText = "") }
    }

    fun closeReplySheet() {
        _uiState.update { it.copy(showReplySheet = false, replyText = "") }
    }

    fun onReplyTextChange(text: String) {
        _uiState.update { it.copy(replyText = text) }
    }

    fun submitReply(commentId: String) {
        val state = _uiState.value
        val comment = state.selectedComment ?: return
        if (state.replyText.isBlank() || state.isSubmittingReply) return

        _uiState.update { it.copy(isSubmittingReply = true) }
        viewModelScope.launch {
            val result = commentRepository.createComment(
                reviewId = comment.reviewId,
                parentCommentId = commentId,
                userId = state.currentUserId,
                content = state.replyText.trim()
            )
            result.onSuccess {
                _uiState.update { s ->
                    s.copy(
                        isSubmittingReply = false,
                        showReplySheet = false,
                        replyText = "",
                        selectedComment = s.selectedComment?.copy(
                            repliesCount = (s.selectedComment.repliesCount) + 1
                        )
                    )
                }
                cargarRespuestas(commentId)
            }.onFailure {
                _uiState.update { it.copy(isSubmittingReply = false) }
            }
        }
    }
}
