package com.example.tuprofe.ui.Config

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ConfigViewModel: ViewModel() {

    private val _uiState = MutableStateFlow(ConfigState())
    val uiState: StateFlow<ConfigState> = _uiState.asStateFlow()

    fun onProfileClick() {
        _uiState.update { it.copy(Profile = true) }
    }

    fun onAyudaClick() {
        _uiState.update { it.copy(Ayuda = true) }
    }

    fun onPrivacidadClick() {
        _uiState.update { it.copy(Privacidad = true) }
    }

    fun onNotificacionesClick() {
        _uiState.update { it.copy(Notificaciones = true) }
    }
}