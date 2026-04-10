package com.example.tuprofe.ui.Main

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
class MainViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainState())
    val uiState: StateFlow<MainState> = _uiState.asStateFlow()

    init {
        obtenerResenas()
    }

    private fun obtenerResenas() {
        viewModelScope.launch {
            val result = reviewRepository.getReviews()
            if (result.isSuccess) {
                _uiState.update {
                    it.copy(
                        reviews = result.getOrNull() ?: emptyList(), isLoading = false, errorMessage = null
                    )
                }
            } else {
                _uiState.update{it.copy(errorMessage = result.exceptionOrNull()?.message, isLoading = false)}

            }
        }
    }
}
