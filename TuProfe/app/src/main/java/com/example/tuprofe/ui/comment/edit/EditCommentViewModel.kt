package com.example.tuprofe.ui.comment.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tuprofe.data.repository.CommentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditCommentViewModel @Inject constructor(
    private val commentRepository: CommentRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditCommentState())
    val uiState: StateFlow<EditCommentState> = _uiState.asStateFlow()

    init {
        val commentId = savedStateHandle.get<String>("commentId") ?: ""
        if (commentId.isNotEmpty()) {
            cargarComentario(commentId)
        }
    }

    private fun cargarComentario(commentId: String) {
        _uiState.update { it.copy(commentId = commentId, isInitialLoading = true) }
        viewModelScope.launch {
            val result = commentRepository.getCommentById(commentId, "")
            if (result.isSuccess) {
                val comment = result.getOrNull()
                if (comment != null) {
                    _uiState.update {
                        it.copy(
                            commentText = comment.content,
                            isInitialLoading = false
                        )
                    }
                } else {
                    _uiState.update { it.copy(error = "Comentario no encontrado", isInitialLoading = false) }
                }
            } else {
                _uiState.update { it.copy(error = "Error al cargar datos", isInitialLoading = false) }
            }
        }
    }

    fun onCommentTextChange(text: String) {
        _uiState.update { it.copy(commentText = text) }
    }

    fun updateComment() {
        val state = _uiState.value
        if (state.commentText.isBlank()) {
            _uiState.update { it.copy(error = "El contenido no puede estar vacío") }
            return
        }
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            val result = commentRepository.updateComment(state.commentId, state.commentText)
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
