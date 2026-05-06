package com.example.tuprofe.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tuprofe.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SplashState())
    val uiState: StateFlow<SplashState> = _uiState.asStateFlow()

    init {
        checkUser()
    }

    private fun checkUser() {
        viewModelScope.launch {
            delay(8000)
            if (authRepository.currentUser != null) {
                _uiState.update { it.copy(navigateToMain = true) }
            } else {
                _uiState.update { it.copy(navigateToLogin = true) }
            }
        }
    }

    fun onNavigationHandled() {
        _uiState.update { it.copy(navigateToMain = false, navigateToLogin = false) }
    }
}
