package com.example.tuprofe.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tuprofe.data.repository.AuthRepository
import com.example.tuprofe.data.repository.ReviewRepository
import com.example.tuprofe.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository,
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainState())
    val uiState: StateFlow<MainState> = _uiState.asStateFlow()

    fun selectTab(index: Int) {
        _uiState.update { it.copy(selectedTab = index) }
    }

    fun fetchReviews() {
        _uiState.update { it.copy(isLoading = true) }
        /*
        viewModelScope.launch {
            Log.d("MainViewModel", "Iniciando obtención de reseñas...")
            val result = reviewRepository.getReviews()
            if (result.isSuccess) {
                val reviews = result.getOrNull() ?: emptyList()
                Log.d("MainViewModel", "Reseñas obtenidas con éxito: ${reviews.size}")

                val currentUserId = authRepository.currentUser?.uid ?: ""
                val followingIds = if (currentUserId.isNotEmpty()) {
                    userRepository.getFollowingIds(currentUserId).getOrDefault(emptyList())
                } else emptyList()

                _uiState.update {
                    it.copy(
                        reviews = reviews,
                        followingReviews = reviews.filter { r -> r.usuario.usuarioId in followingIds },
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
        } */

        viewModelScope.launch {
            reviewRepository.getReviewsLive()
                .catch { e -> _uiState.update { it.copy(errorMessage = e.message, isLoading = false) } }
                .collect { reviews ->

                    val reviews = reviews
                    Log.d("MainViewModel", "Reseñas obtenidas con éxito: ${reviews.size}")

                    val currentUserId = authRepository.currentUser?.uid ?: ""
                    val followingIds = if (currentUserId.isNotEmpty()) {
                        userRepository.getFollowingIds(currentUserId).getOrDefault(emptyList())
                    } else emptyList()

                    _uiState.update {
                        it.copy(
                            reviews = reviews,
                            followingReviews = reviews.filter { r -> r.usuario.usuarioId in followingIds },
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                }
        }


    }
}