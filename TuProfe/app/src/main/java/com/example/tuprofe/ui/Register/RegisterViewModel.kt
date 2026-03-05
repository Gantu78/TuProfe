package com.example.tuprofe.ui.Register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class RegisterViewModel: ViewModel() {

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

    fun setErrorMessage(newErrorMessage: String) {
        _uiState.update { it.copy(errorMessage = newErrorMessage) }
    }

    fun onRegisterClick(){

        if(_uiState.value.password1.isNullOrEmpty() || _uiState.value.password2.isNullOrEmpty()|| _uiState.value.email.isNullOrEmpty()||_uiState.value.usuario.isNullOrEmpty()||_uiState.value.carrera.isNullOrEmpty()){
            _uiState.update{ it.copy(mostrarMensajeError = true, errorMessage = "Por favor complete todos los campos") }

        } else if(_uiState.value.password1 != _uiState.value.password2){
            _uiState.update{ it.copy(mostrarMensajeError = true, errorMessage = "Las contraseñas no coinciden") }
        } else {
            _uiState.update{ it.copy(mostrarMensajeError = false, mostrarMensaje = true, navigateHome = true) }
        }
    }

    fun onAlreadyAccountClick(){
        _uiState.update{ it.copy(navigateHome = true) }
    }

    fun onBackClick(){
        _uiState.update{ it.copy(navigateHome = true) }
    }


    }