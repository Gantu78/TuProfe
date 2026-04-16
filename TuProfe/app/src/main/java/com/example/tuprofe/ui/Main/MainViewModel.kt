package com.example.tuprofe.ui.Main

import android.util.Log
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
class MainViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainState())
    val uiState: StateFlow<MainState> = _uiState.asStateFlow()



     fun fetchReviews() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            Log.d("MainViewModel", "Iniciando obtención de reseñas...")
            val result = reviewRepository.getReviews()
            if (result.isSuccess) {
                val reviews = result.getOrNull() ?: emptyList()
                Log.d("MainViewModel", "Reseñas obtenidas con éxito: ${reviews.size}")
                _uiState.update {
                    it.copy(
                        reviews = reviews,
                        isLoading = false,
                        errorMessage = null
                    )
                }
            } else {
                val error = result.exceptionOrNull()
                Log.e("MainViewModel", "Error al obtener reseñas", error)
                _uiState.update {
                    it.copy(
                        errorMessage = error?.message ?: "Error desconocido",
                        isLoading = false
                    )
                }
            }
        }
    }
}
