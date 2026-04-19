package com.example.tuprofe.ui.configPerfil

import android.net.Uri
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
class ConfigPerfilViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val storageRepository: StorageRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ConfigPerfilState())
    val uiState: StateFlow<ConfigPerfilState> = _uiState.asStateFlow()

    init {
        _uiState.update {
            it.copy(
                email = authRepository.currentUser?.email ?: "",
                profileImage = authRepository.currentUser?.photoUrl?.toString() ?: ""
            )
        }
        loadUserProfile()
    }

    private fun loadUserProfile() {
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
                            errorMessagePerfil = error.message
                        )
                    }
                }
            }
        }
    }

    fun setEmail(newEmail: String) {
        _uiState.update { it.copy(email = newEmail) }
    }

    fun setUsuario(newUsuario: String) {
        _uiState.update { it.copy(username = newUsuario) }
    }

    fun setCarrera(newCarrera: String) {
        _uiState.update { it.copy(carrera = newCarrera) }
    }

    fun onBorrarCuentaClick(email: String, password: String) {
        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessageEliminar = null
                )
            }

            val result = authRepository.deleteAccount(email, password)

            result
                .onSuccess {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        navigate = true,
                        showDeleteDialog = false
                    )
                }
            }
                .onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessageEliminar = error.message
                    )
                }
            }
        }
    }

    fun onGuardarCambiosClick() {
        val userId = authRepository.currentUser?.uid ?: return
        viewModelScope.launch {
            val current = _uiState.value
            _uiState.update { it.copy(isLoading = true, errorMessagePerfil = null, showSaveDialog = false) }
            val result = userRepository.updateUser(
                userId = userId,
                username = current.username,
                email = current.email,
                carrera = current.carrera
            )
            result.onSuccess {
                _uiState.update { it.copy(isLoading = false, saveSuccess = true) }
            }.onFailure { error ->
                _uiState.update { it.copy(isLoading = false, errorMessagePerfil = error.message) }
            }
        }
    }

    fun onSaveSuccessDone() {
        _uiState.update { it.copy(saveSuccess = false) }
    }

    fun toggleShowDelete() {
        _uiState.update {
            it.copy(showDeleteDialog = !it.showDeleteDialog)
        }
    }

    fun toggleShowSave() {
        _uiState.update {
            it.copy(showSaveDialog = !it.showSaveDialog)
        }
    }

    fun onNavigationDone() {
        _uiState.update { it.copy(navigate = false) }
    }

    fun clearErrorEliminar() {
        _uiState.update { it.copy(errorMessageEliminar = null) }
    }

    fun uploadImageToFirebase(imagen: Uri){
        val userId = authRepository.currentUser?.uid ?: return
        viewModelScope.launch {
            val result = storageRepository.uploadProfileImage(imagen)
            result.onSuccess { imageUrl ->
                _uiState.update { it.copy(profileImage = imageUrl) }
                userRepository.updateUserPhoto(userId, imageUrl)
            }
            result.onFailure { error ->
                _uiState.update { it.copy(errorMessagePerfil = error.message) }
            }
        }
    }
}
