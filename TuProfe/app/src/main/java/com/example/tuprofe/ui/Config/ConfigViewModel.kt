package com.example.tuprofe.ui.Config

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tuprofe.data.repository.AuthRepository
import com.example.tuprofe.data.repository.StorageRepository
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
    private val storageRepository: StorageRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ConfigState(
        email = authRepository.currentUser?.email ?: "",
        profileImageUrl = authRepository.currentUser?.photoUrl?.toString() ?: ""
    ))
    val uiState: StateFlow<ConfigState> = _uiState.asStateFlow()

    fun onLogoutClick() {
        authRepository.signOut()
    }


    fun onAyudaClick() {

    }

    fun onPrivacidadClick() {

    }

    fun onNotificacionesClick() {

    }

    init {
        loadUserProfile()
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