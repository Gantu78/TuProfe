package com.example.tuprofe.ui.register

import com.example.tuprofe.R
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

    fun onSuccessDialogDismissed() {
        _uiState.update { it.copy(mostrarMensaje = false, navigateHome = true) }
    }

    fun togglePasswordVisibility() {
        _uiState.update { it.copy(passwordVisible = !_uiState.value.passwordVisible) }
    }


    fun onRegisterClickSecure() {
        val currentState = _uiState.value


        if (currentState.email.isBlank() || currentState.usuario.isBlank() ||
            currentState.password1.isBlank() || currentState.password2.isBlank()) {
            _uiState.update {
                it.copy(mostrarMensajeError = true, errorMessage = R.string.por_favor_completa_campos)
            }
            return
        }

        if (currentState.password1 != currentState.password2) {
            _uiState.update {
                it.copy(mostrarMensajeError = true, errorMessage = R.string.las_contrase_as_no_coinciden)
            }
            return
        }

        viewModelScope.launch(ioDispatcher) {
            _uiState.update { it.copy(isLoading = true, mostrarMensajeError = false) }

            val result = authRepository.signUp(currentState.email, currentState.password1)

            if (result.isSuccess) {
                val userId = authRepository.currentUser?.uid ?: ""


                val firestoreResult = userRepository.registerUser(
                    username = currentState.usuario,
                    carrera = currentState.carrera,
                    userId = userId
                )

                if (firestoreResult.isSuccess) {
                    authRepository.sendEmailVerification()
                    _uiState.update { it.copy(isLoading = false, mostrarMensaje = true) }
                } else {
                    val error = firestoreResult.exceptionOrNull()?.message ?: "Error al guardar perfil"
                    _uiState.update { it.copy(isLoading = false, mostrarMensajeError = true, errorMessage = R.string.error_al_procesar_solicitud) }
                }
            } else {
                val error = result.exceptionOrNull()?.message ?: "Error en el registro"
                _uiState.update { it.copy(isLoading = false, mostrarMensajeError = true, errorMessage = R.string.error_al_procesar_solicitud) }
            }
        }
    }
}