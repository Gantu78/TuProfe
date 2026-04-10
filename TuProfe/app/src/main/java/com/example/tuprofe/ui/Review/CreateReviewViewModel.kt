package com.example.tuprofe.ui.Review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tuprofe.data.repository.ReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateReviewViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateReviewState())
    val uiState: StateFlow<CreateReviewState> = _uiState.asStateFlow()

    fun onReviewTextChange(newText: String) {
        _uiState.update { it.copy(reviewText = newText) }
    }

    fun createReview() {
        viewModelScope.launch{ }
        val result = reviewRepository.createReview(_uiState.value.reviewText, profesor, username, materiaId, content) //toca corregir esto
        if (result.isSuccess) {
            _uiState.update { it.copy(isLoading = true) }
        } else {
            _uiState.update { it.copy(error = result.exceptionOrNull()?.message) }
        }
    }
}
