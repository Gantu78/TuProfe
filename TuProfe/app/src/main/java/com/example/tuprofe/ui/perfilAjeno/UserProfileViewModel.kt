package com.example.tuprofe.ui.perfilAjeno

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tuprofe.data.Usuario
import com.example.tuprofe.data.repository.AuthRepository
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
    private val authRepository: AuthRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserProfileState())
    val uiState: StateFlow<UserProfileState> = _uiState.asStateFlow()

    init {
        _uiState.update { it.copy(currentUserId = authRepository.currentUser?.uid ?: "") }
        val userId = savedStateHandle.get<String>("userId") ?: ""
        if (userId.isNotEmpty()) {
            cargarDatosUsuario(userId)
        }
    }

    private fun cargarDatosUsuario(userId: String) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val userResult = userRepository.getUserById(userId)
            val reviewsResult = reviewRepository.getUserReviews(userId)

            if (userResult.isSuccess && reviewsResult.isSuccess) {
                _uiState.update {
                    it.copy(
                        user = userResult.getOrNull()?: Usuario("", "", "", "", "", 0, 0, false),
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

    fun followOrUnfollowUser(targetUserId: String) {
        val currentUserId = _uiState.value.currentUserId
        viewModelScope.launch {
            val result = userRepository.followOrUnfollow(currentUserId, targetUserId)
            if (result.isSuccess) {
                val user = _uiState.value.user
                _uiState.update {
                    it.copy(
                        user = user.copy(
                            followersCount = if (user.followed) user.followersCount - 1 else user.followersCount + 1,
                            followed = !user.followed
                        )
                    )
                }
            }
        }
    }
}
