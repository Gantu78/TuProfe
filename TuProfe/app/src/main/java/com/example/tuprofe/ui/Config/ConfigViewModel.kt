package com.example.tuprofe.ui.Config

import androidx.lifecycle.ViewModel
import com.example.tuprofe.data.repository.AuthRepository
import com.example.tuprofe.data.repository.StorageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ConfigViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val storageRepository: StorageRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ConfigState(
        email = authRepository.currentUser?.email ?: "",
        profileImageUrl = authRepository.currentUser?.photoUrl?.toString() ?: ""
    ))
    val uiState: StateFlow<ConfigState> = _uiState.asStateFlow()

    fun onLogoutClick() {
        authRepository.signOut()
    }


    fun onAyudaClick() {

    }

    fun onPrivacidadClick() {

    }

    fun onNotificacionesClick() {

    }
}