package com.example.tuprofe.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tuprofe.data.repository.AuthRepository
import com.example.tuprofe.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.util.Log
import com.example.tuprofe.data.injection.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers


@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
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

        viewModelScope.launch(ioDispatcher) {
            registerUserOnline()
        }
    }

    suspend fun registerUserOnline(){
        val currentState = _uiState.value
        if (
            currentState.password1.isBlank() ||
            currentState.password2.isBlank() ||
            currentState.email.isBlank() ||
            currentState.usuario.isBlank() ||
            currentState.carrera.isBlank()
        ) {
            _uiState.update {
                it.copy(
                    mostrarMensajeError = true,
                    errorMessage = "Por favor complete todos los campos"
                )
            }
            return
        }

        if (currentState.password1 != currentState.password2) {
            _uiState.update {
                it.copy(
                    mostrarMensajeError = true,
                    errorMessage = "Las contraseñas no coinciden"
                )
            }
            return
        }

        val result = authRepository.signUp(
            currentState.email,
            currentState.password1
        )

        if (result.isSuccess) {
            val userId = authRepository.currentUser?.uid ?: throw Exception("No se pudo obtener el usuario actual")

            val firestoreResult = userRepository.registerUser(
                username = currentState.usuario,
                carrera = currentState.carrera,
                userId = userId
            )

            if (firestoreResult.isSuccess) {
                _uiState.update {
                    it.copy(
                        mostrarMensajeError = false,
                        mostrarMensaje = true,
                        navigateHome = true
                    )
                }
            } else {
                val errorMessage =
                    firestoreResult.exceptionOrNull()?.message ?: "Error al guardar el perfil"
                Log.e("RegisterViewModel", "Firestore Error: $errorMessage") // Add this line
                _uiState.update {
                    it.copy(
                        mostrarMensajeError = true,
                        errorMessage = errorMessage
                    )
                }
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
