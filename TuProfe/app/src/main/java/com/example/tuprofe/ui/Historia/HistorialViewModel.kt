package com.example.tuprofe.ui.Historia

import androidx.lifecycle.ViewModel
import com.example.tuprofe.data.ReviewInfo
import com.example.tuprofe.data.local.LocalReview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HistorialViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(HistorialState())
    val uiState: StateFlow<HistorialState> = _uiState

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

    fun onFilterClick(filter: String) {
        _uiState.update { it.copy(selectedFilter = filter) }
    }

}
