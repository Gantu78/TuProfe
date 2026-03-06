package com.example.tuprofe.ui.ConfigPerfil

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ConfigPerfilViewModel: ViewModel() {

    private val _uiState = MutableStateFlow(ConfigPerfilState())
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
        _uiState.update { it.copy(showDeleteDialog = true) }

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
