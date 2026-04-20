package com.example.tuprofe.ui.config

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tuprofe.data.repository.AuthRepository
import com.example.tuprofe.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfigViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ConfigState())
    val uiState: StateFlow<ConfigState> = _uiState.asStateFlow()

    fun onLogoutClick() {
        authRepository.signOut()
    }


    fun onAyudaClick() {

    }

    fun onPrivacidadClick() {

    }

    fun onAjustesClick() {

    }

    init {
        _uiState.update {
            it.copy(
                email = authRepository.currentUser?.email ?: "",
                profileImageUrl = authRepository.currentUser?.photoUrl?.toString() ?: ""
            )
        }
        loadUserProfile()
    }

    fun openFollowersSheet() {
        val userId = authRepository.currentUser?.uid ?: return
        _uiState.update { it.copy(showFollowersSheet = true, isLoadingList = true) }
        viewModelScope.launch {
            userRepository.getFollowers(userId, userId).onSuccess { list ->
                _uiState.update { it.copy(followersList = list, isLoadingList = false) }
            }.onFailure {
                _uiState.update { it.copy(isLoadingList = false) }
            }
        }
    }

    fun openFollowingSheet() {
        val userId = authRepository.currentUser?.uid ?: return
        _uiState.update { it.copy(showFollowingSheet = true, isLoadingList = true) }
        viewModelScope.launch {
            userRepository.getFollowing(userId, userId).onSuccess { list ->
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
                fun toggle(list: List<com.example.tuprofe.data.Usuario>) = list.map { u ->
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

    fun loadUserProfile() {
        val userId = authRepository.currentUser?.uid
        if (userId != null) {
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true) }
                val result = userRepository.getUserById(userId)
                result.onSuccess { usuario ->
                    _uiState.update {
                        it.copy(
                            username = usuario.nombreUsu,
                            carrera = usuario.carrera,
                            profileImageUrl = usuario.imageprofeUrl,
                            followersCount = usuario.followersCount,
                            followingCount = usuario.followingCount,
                            isLoading = false
                        )
                    }
                }.onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message
                        )
                    }
                }
            }
        }
    }
}