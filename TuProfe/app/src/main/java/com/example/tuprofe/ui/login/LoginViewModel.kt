package com.example.tuprofe.ui.login

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
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(LoginState())
    val uiState: StateFlow<LoginState> = _uiState

    fun setEmail(newEmail: String) {
        _uiState.update { it.copy(email = newEmail) }
    }

    fun setPassword(newPassword: String) {
        _uiState.update { it.copy(password = newPassword) }
    }

    fun togglePasswordVisibility() {
        _uiState.update { it.copy(passwordVisible = !_uiState.value.passwordVisible) }
    }

    fun loginClick() {
        if (_uiState.value.email.isNullOrEmpty() || _uiState.value.password.isNullOrEmpty()) {
            _uiState.update {
                it.copy(
                    mostrarMensajeError = true,
                    errorMessage = "Por favor complete todos los campos"
                )
            }
            return
        }
        viewModelScope.launch {
            val result = authRepository.signIn(_uiState.value.email, _uiState.value.password)

            if (result.isSuccess) {

                if (authRepository.isEmailVerified()) {
                    _uiState.update { it.copy(mostrarMensajeError = false, navigate = true) }
                } else {

                    authRepository.signOut()
                    _uiState.update {
                        it.copy(
                            mostrarMensajeError = true,
                            errorMessage = "Debes verificar tu correo electrónico antes de poder ingresar."
                        )
                    }
                }
            } else {
                val mensaje = result.exceptionOrNull()?.message ?: "Error al iniciar sesión"
                _uiState.update {
                    it.copy(
                        mostrarMensajeError = true,
                        errorMessage = mensaje
                    )
                }
            }
        }
    }
}