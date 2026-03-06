package com.example.tuprofe.ui.ResetPassword

import androidx.lifecycle.ViewModel
import com.example.tuprofe.ui.Register.RegisterState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class ResetPasswordViewModel: ViewModel() {

    private val _uiState = MutableStateFlow(ResetPasswordState())
    val uiState: StateFlow<ResetPasswordState> = _uiState

    fun setEmail(newEmail: String) {
        _uiState.update { it.copy(email = newEmail) }
    }

    fun toggleMostrarMensaje() {
        _uiState.update { it.copy(mostrarMensaje = !_uiState.value.mostrarMensaje) }
    }

}
