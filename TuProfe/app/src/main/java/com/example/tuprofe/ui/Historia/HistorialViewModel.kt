package com.example.tuprofe.ui.Historia

import androidx.lifecycle.ViewModel
import com.example.tuprofe.data.local.LocalReview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HistorialViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(HistorialState())
    val uiState: StateFlow<HistorialState> = _uiState.asStateFlow()

    init {
        cargarHistorial()
    }

    private fun cargarHistorial() {
        _uiState.update { it.copy(isLoading = true) }
        _uiState.update { it.copy(
            userReviews = LocalReview.Reviews,
            isLoading = false
        ) }
    }

    fun onReviewClick(reviewId: Int) {
        _uiState.update { it.copy(navigateToReviewId = reviewId) }
    }

    fun onNavigationHandled() {
        _uiState.update { it.copy(navigateToReviewId = null) }
    }
}
