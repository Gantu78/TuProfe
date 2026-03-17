package com.example.tuprofe.ui.ConfigPerfil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tuprofe.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfigPerfilViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(ConfigPerfilState(
        email = authRepository.currentUser?.email ?: ""
    ))
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

    fun onBorrarCuentaClick () {
        viewModelScope.launch {
            val result = authRepository.deleteAccount()

            if(result.isSuccess){
                _uiState.update { it.copy(navigate = true) }
                _uiState.update { it.copy(showDeleteDialog = false) }
            }

        }
    }

    fun onGuardarCambiosClick() {
        _uiState.update { it.copy(navigate = true )}
    }

    fun toggleShowDelete() {
        _uiState.update { it.copy(showDeleteDialog = !_uiState.value.showDeleteDialog) }
    }

    fun toggleShowSave() {
        _uiState.update { it.copy(showSaveDialog = !_uiState.value.showSaveDialog) }
    }





}
