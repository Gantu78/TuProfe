package com.example.tuprofe.ui.ConfigPerfil

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tuprofe.data.repository.AuthRepository
import com.example.tuprofe.data.repository.StorageRepository
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
    private val storageRepository: StorageRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        ConfigPerfilState(
            email = authRepository.currentUser?.email ?: "",
            profileImage = authRepository.currentUser?.photoUrl?.toString() ?: ""
        )
    )
    val uiState: StateFlow<ConfigPerfilState> = _uiState.asStateFlow()

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
                    errorMessage = null
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
                        errorMessage = error.message
                    )
                }
            }
        }
    }

    fun onGuardarCambiosClick() {
        _uiState.update {
            it.copy(
                navigate = true,
                showSaveDialog = false
            )
        }
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

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun uploadImageToFirebase(Imagen: Uri){
        viewModelScope.launch {
           val result = storageRepository.uploadProfileImage(Imagen)
            result.onSuccess {
                _uiState.update {
                    it.copy(
                        profileImage = result.getOrNull()
                    )
                }
            }
            result.onFailure {
                _uiState.update {
                    it.copy(
                        errorMessage = it.errorMessage
                    )
                }
            }

        }
    }



}
