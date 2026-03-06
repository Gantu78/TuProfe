package com.example.tuprofe.ui.Main

import androidx.lifecycle.ViewModel
import com.example.tuprofe.data.local.LocalReview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MainState())
    val uiState: StateFlow<MainState> = _uiState.asStateFlow()

    init {
        obtenerResenas()
    }

    private fun obtenerResenas() {
        _uiState.update { it.copy(isLoading = true) }
        _uiState.update { it.copy(
            reviews = LocalReview.Reviews,
            isLoading = false
        ) }
    }
}
