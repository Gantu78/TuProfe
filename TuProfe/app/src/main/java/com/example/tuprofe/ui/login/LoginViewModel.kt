package com.example.tuprofe.ui.login

import android.app.Activity
import android.util.Log
import com.example.tuprofe.R
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
import kotlinx.coroutines.Job
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
                    errorMessage = R.string.por_favor_completa_campos
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
                            errorMessage = R.string.debes_verificar_correo
                        )
                    }
                }
            } else {
                _uiState.update {
                    it.copy(mostrarMensajeError = true, errorMessage = R.string.error_al_iniciar_sesion)
                }
            }
        }
    }


    private var googleSignInJob: Job? = null
    private var gitHubSignInJob: Job? = null

    fun signInWithGoogle(activity: Activity) {
        googleSignInJob?.cancel()
        _uiState.update { it.copy(isGoogleLoading = true, mostrarMensajeError = false) }
        googleSignInJob = viewModelScope.launch {
            val provider = OAuthProvider.newBuilder("google.com").build()
            val result = authRepository.signInWithProvider(activity, provider)
            if (result.isSuccess) {
                _uiState.update { it.copy(isGoogleLoading = false, navigate = true) }
            } else {
                _uiState.update {
                    it.copy(
                        isGoogleLoading = false,
                        mostrarMensajeError = true,
                        errorMessage = R.string.error_google_signin
                    )
                }
            }
        }
    }

    fun signInWithGitHub(activity: Activity) {
        gitHubSignInJob?.cancel()
        _uiState.update { it.copy(isGitHubLoading = true, mostrarMensajeError = false) }
        gitHubSignInJob = viewModelScope.launch {
            val result = authRepository.signInWithGitHub(activity)
            if (result.isSuccess) {
                _uiState.update { it.copy(isGitHubLoading = false, navigate = true) }
            } else {
                _uiState.update {
                    it.copy(
                        isGitHubLoading = false,
                        mostrarMensajeError = true,
                        errorMessage = R.string.error_github_signin
                    )
                }
            }
        }
    }

    // Llamar cuando la pantalla vuelve al frente: cancela cualquier OAuth abandonado
    fun resetSocialLoadingIfPending() {
        if (googleSignInJob?.isActive == true) {
            googleSignInJob?.cancel()
            _uiState.update { it.copy(isGoogleLoading = false) }
        }
        if (gitHubSignInJob?.isActive == true) {
            gitHubSignInJob?.cancel()
            _uiState.update { it.copy(isGitHubLoading = false) }
        }
    }
}
