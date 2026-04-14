package com.example.tuprofe.ui.Detalle

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tuprofe.data.local.LocalReview
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
    private val savedStateHandle: SavedStateHandle,
    private val reviewRepository: ReviewRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetalleState())
    val uiState: StateFlow<DetalleState> = _uiState.asStateFlow()

    init {
        val reviewId = savedStateHandle.get<String>("reviewId") ?: ""
        Log.d("DetalleViewModel", "Init con reviewId: $reviewId")
        if (reviewId.isNotEmpty()) {
            cargarDetalle(reviewId)
        }
    }

    private fun cargarDetalle(reviewId: String) {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            try {
                val result = reviewRepository.getReviews()
                
                if (result.isSuccess) {
                    val allReviews = result.getOrNull() ?: emptyList()
                    val selected = allReviews.find { it.reviewId == reviewId }
                    val respuestas = allReviews.filter { it.reviewId != reviewId }

                    _uiState.update {
                        it.copy(
                            selectedReview = selected,
                            respuestas = respuestas,
                            isLoading = false
                        )
                    }
                } else {
                    // Fallback
                    val selected = LocalReview.Reviews.find { it.reviewId == reviewId }
                    val respuestas = LocalReview.Reviews.filter { it.reviewId != reviewId }
                    _uiState.update {
                        it.copy(
                            selectedReview = selected,
                            respuestas = respuestas,
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e("DetalleViewModel", "Error cargando detalle", e)
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}
