package com.example.tuprofe.ui.resetPassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tuprofe.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(ResetPasswordState())
    val uiState: StateFlow<ResetPasswordState> = _uiState

    fun setEmail(newEmail: String) {
        _uiState.update { it.copy(email = newEmail) }
    }

    fun resetPassword() {
        val email = _uiState.value.email
        if (email.isBlank()) {
            _uiState.update { it.copy(mostrarError = true, mostrarMensaje = false, errorMessage = "Por favor ingresa tu correo") }
            return
        }
        viewModelScope.launch {
            val result = authRepository.resetPassword(email)
            if (result.isSuccess) {
                _uiState.update { it.copy(mostrarMensaje = true, mostrarError = false) }
            } else {
                val mensaje = result.exceptionOrNull()?.message ?: "Error al enviar el enlace"
                _uiState.update { it.copy(mostrarError = true, mostrarMensaje = false, errorMessage = mensaje) }
            }
        }
    }

}
