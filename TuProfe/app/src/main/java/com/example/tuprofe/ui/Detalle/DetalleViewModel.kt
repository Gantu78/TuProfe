package com.example.tuprofe.ui.Detalle

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.tuprofe.data.local.LocalReview
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class DetalleViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {

    private val _uiState = MutableStateFlow(DetalleState())
    val uiState: StateFlow<DetalleState> = _uiState.asStateFlow()

    init {
        savedStateHandle.get<Int>("reviewId")?.let { cargarDetalle(it) }
    }

    private fun cargarDetalle(reviewId: Int) {
        _uiState.update { it.copy(isLoading = true) }

        val review = LocalReview.Reviews.find { it.reviewId == reviewId }
        val respuestas = LocalReview.Reviews.filter { it.reviewId != reviewId }

        _uiState.update {
            it.copy(
                selectedReview = review,
                respuestas = respuestas,
                isLoading = false
            )
        }
    }

}
