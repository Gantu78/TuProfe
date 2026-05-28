package com.example.tuprofe.ui.login

import android.app.Activity
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tuprofe.BuildConfig
import com.example.tuprofe.data.repository.AuthRepository
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.OAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginState())
    val uiState: StateFlow<LoginState> = _uiState

    // ── Email / password ──────────────────────────────────────────────────────

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
                    it.copy(mostrarMensajeError = true, errorMessage = mensaje)
                }
            }
        }
    }


    fun signInWithGoogle(activity: Activity) {
        _uiState.update { it.copy(isGoogleLoading = true, mostrarMensajeError = false) }

        viewModelScope.launch {

            val provider = OAuthProvider.newBuilder("google.com").build()

            val result = authRepository.signInWithProvider(activity, provider)

            if (result.isSuccess) {
                _uiState.update { it.copy(isGoogleLoading = false, navigate = true) }
            } else {
                _uiState.update {
                    it.copy(
                        isGoogleLoading = false,
                        mostrarMensajeError = true,
                        errorMessage = result.exceptionOrNull()?.message ?: "Error al iniciar con Google"
                    )
                }
            }
        }
    }


    fun signInWithGitHub(activity: Activity) {
        _uiState.update { it.copy(isGitHubLoading = true, mostrarMensajeError = false) }
        viewModelScope.launch {
            val result = authRepository.signInWithGitHub(activity)
            if (result.isSuccess) {
                _uiState.update { it.copy(isGitHubLoading = false, navigate = true) }
            } else {
                _uiState.update {
                    it.copy(
                        isGitHubLoading = false,
                        mostrarMensajeError = true,
                        errorMessage = result.exceptionOrNull()?.message ?: "Error con GitHub"
                    )
                }
            }
        }
    }
}
