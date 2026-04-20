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
        val currentUserId = authRepository.currentUser?.uid ?: ""
        viewModelScope.launch {
            val userResult = userRepository.getUserById(userId, currentUserId)
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
        val currentUserId = authRepository.currentUser?.uid ?: return
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

    fun openFollowersSheet() {
        val state = _uiState.value
        val currentUserId = authRepository.currentUser?.uid ?: ""
        _uiState.update { it.copy(showFollowersSheet = true, isLoadingList = true) }
        viewModelScope.launch {
            val result = userRepository.getFollowers(state.user.usuarioId, currentUserId)
            result.onSuccess { list ->
                _uiState.update { it.copy(followersList = list, isLoadingList = false) }
            }.onFailure {
                _uiState.update { it.copy(isLoadingList = false) }
            }
        }
    }

    fun openFollowingSheet() {
        val state = _uiState.value
        val currentUserId = authRepository.currentUser?.uid ?: ""
        _uiState.update { it.copy(showFollowingSheet = true, isLoadingList = true) }
        viewModelScope.launch {
            val result = userRepository.getFollowing(state.user.usuarioId, currentUserId)
            result.onSuccess { list ->
                _uiState.update { it.copy(followingList = list, isLoadingList = false) }
            }.onFailure {
                _uiState.update { it.copy(isLoadingList = false) }
            }
        }
    }

    fun closeSheet() {
        _uiState.update { it.copy(showFollowersSheet = false, showFollowingSheet = false) }
    }

    fun followOrUnfollowInList(targetUserId: String) {
        val currentUserId = authRepository.currentUser?.uid ?: return
        viewModelScope.launch {
            val result = userRepository.followOrUnfollow(currentUserId, targetUserId)
            if (result.isSuccess) {
                fun toggle(list: List<Usuario>) = list.map { u ->
                    if (u.usuarioId == targetUserId) u.copy(
                        followed = !u.followed,
                        followersCount = if (u.followed) u.followersCount - 1 else u.followersCount + 1
                    ) else u
                }
                _uiState.update { state ->
                    state.copy(
                        followersList = toggle(state.followersList),
                        followingList = toggle(state.followingList)
                    )
                }
            }
        }
    }
}
