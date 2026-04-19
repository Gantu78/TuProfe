package com.example.tuprofe.ui.perfilAjeno

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tuprofe.data.repository.ReviewRepository
import com.example.tuprofe.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository,
    private val userRepository: UserRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserProfileState())
    val uiState: StateFlow<UserProfileState> = _uiState.asStateFlow()

    init {
        // Obtenemos el ID como String de la navegación
        val userId = savedStateHandle.get<String>("userId") ?: ""
        if (userId.isNotEmpty()) {
            cargarDatosUsuario(userId)
        }
    }

    private fun cargarDatosUsuario(userId: String) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            // El Repositorio recibe String y se encarga de la conversión a Int para Retrofit
            val userResult = userRepository.getUserById(userId)
            val reviewsResult = reviewRepository.getUserReviews(userId)

            if (userResult.isSuccess && reviewsResult.isSuccess) {
                _uiState.update {
                    it.copy(
                        user = userResult.getOrNull(),
                        userReviews = reviewsResult.getOrNull() ?: emptyList(),
                        isLoading = false,
                        errorMessage = null
                    )
                }
            } else {
                val error = userResult.exceptionOrNull() ?: reviewsResult.exceptionOrNull()
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error?.message ?: "Error al cargar el perfil"
                    )
                }
            }
        }
    }
}
