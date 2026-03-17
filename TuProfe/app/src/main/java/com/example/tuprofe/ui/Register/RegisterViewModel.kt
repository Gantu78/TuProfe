package com.example.tuprofe.ui.Register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(RegisterState())
    val uiState: StateFlow<RegisterState> = _uiState

    fun setEmail(newEmail: String) {
        _uiState.update { it.copy(email = newEmail) }
    }

    fun setUsuario(newUsuario: String) {
        _uiState.update { it.copy(usuario = newUsuario) }
    }

    fun setCarrera(newCarrera: String) {
        _uiState.update { it.copy(carrera = newCarrera) }
    }

    fun setPassword1(newPassword: String) {
        _uiState.update { it.copy(password1 = newPassword) }
    }

    fun setPassword2(newPassword: String) {
        _uiState.update { it.copy(password2 = newPassword) }
    }

    fun togglePasswordVisibility() {
        _uiState.update { it.copy(passwordVisible = !_uiState.value.passwordVisible) }
    }


    fun onRegisterClickSecure() {

        if (
            _uiState.value.password1.isBlank() ||
            _uiState.value.password2.isBlank() ||
            _uiState.value.email.isBlank() ||
            _uiState.value.usuario.isBlank() ||
            _uiState.value.carrera.isBlank()
        ) {
            _uiState.update {
                it.copy(
                    mostrarMensajeError = true,
                    errorMessage = "Por favor complete todos los campos"
                )
            }
            return
        }

        if (_uiState.value.password1 != _uiState.value.password2) {
            _uiState.update {
                it.copy(
                    mostrarMensajeError = true,
                    errorMessage = "Las contraseñas no coinciden"
                )
            }
            return
        }

        viewModelScope.launch {

            val result = authRepository.signUp(
                _uiState.value.email,
                _uiState.value.password1
            )

            if (result.isSuccess) {
                _uiState.update {
                    it.copy(
                        mostrarMensajeError = false,
                        mostrarMensaje = true,
                        navigateHome = true
                    )
                }
            } else {
                val errorMessage =
                    result.exceptionOrNull()?.message ?: "Error al registrar el usuario"

                _uiState.update {
                    it.copy(
                        mostrarMensajeError = true,
                        errorMessage = errorMessage
                    )
                }
            }
        }
    }
}

