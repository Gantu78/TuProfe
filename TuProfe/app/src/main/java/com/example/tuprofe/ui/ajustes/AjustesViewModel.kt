package com.example.tuprofe.ui.ajustes

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tuprofe.data.language.LanguageManager
import com.example.tuprofe.data.repository.AuthRepository
import com.example.tuprofe.data.repository.ModerationRepository
import com.example.tuprofe.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AjustesViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
    private val moderationRepository: ModerationRepository
) : ViewModel() {

    private val prefs = context.getSharedPreferences("ajustes_privacidad_prefs", Context.MODE_PRIVATE)

    private val _uiState = MutableStateFlow(
        AjustesState(
            perfilAnonimo    = prefs.getBoolean("perfil_anonimo", false),
            perfilPublico    = prefs.getBoolean("perfil_publico", true),
            resenasEnPerfil  = prefs.getBoolean("resenas_en_perfil", true),
            selectedLanguage = LanguageManager.getSaved(context)   // ← cargar idioma guardado
        )
    )
    val uiState: StateFlow<AjustesState> = _uiState.asStateFlow()

    init {
        loadFromFirestore()
        loadBlockedUsers()
    }

    fun loadBlockedUsers() {
        val userId = authRepository.currentUser?.uid ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingBlocked = true) }
            val list = try { moderationRepository.getBlockedUsers(userId) } catch (_: Exception) { emptyList() }
            _uiState.update { it.copy(blockedUsers = list, isLoadingBlocked = false) }
        }
    }

    fun unblockUser(blockedId: String) {
        val userId = authRepository.currentUser?.uid ?: return
        viewModelScope.launch {
            try {
                moderationRepository.unblockUser(userId, blockedId)
                _uiState.update { state ->
                    state.copy(blockedUsers = state.blockedUsers.filter { it.userId != blockedId })
                }
            } catch (_: Exception) {}
        }
    }



    private fun loadFromFirestore() {
        val userId = authRepository.currentUser?.uid ?: return
        viewModelScope.launch {
            userRepository.getUserById(userId).onSuccess { usuario ->
                val anonimo = usuario.perfilAnonimo
                val publico = usuario.perfilPublico
                val resenas = usuario.resenasEnPerfil
                prefs.edit()
                    .putBoolean("perfil_anonimo", anonimo)
                    .putBoolean("perfil_publico", publico)
                    .putBoolean("resenas_en_perfil", resenas)
                    .apply()
                _uiState.update {
                    it.copy(perfilAnonimo = anonimo, perfilPublico = publico, resenasEnPerfil = resenas)
                }
            }
        }
    }

    private fun persistToFirestore() {
        val userId = authRepository.currentUser?.uid ?: return
        val snapshot = _uiState.value
        viewModelScope.launch {
            userRepository.updatePrivacySettings(
                userId         = userId,
                perfilAnonimo  = snapshot.perfilAnonimo,
                perfilPublico  = snapshot.perfilPublico,
                resenasEnPerfil = snapshot.resenasEnPerfil
            )
        }
    }

    fun togglePerfilAnonimo() {
        val new = !_uiState.value.perfilAnonimo
        prefs.edit().putBoolean("perfil_anonimo", new).apply()
        _uiState.update { it.copy(perfilAnonimo = new) }
        persistToFirestore()
    }

    fun togglePerfilPublico() {
        val new = !_uiState.value.perfilPublico
        prefs.edit().putBoolean("perfil_publico", new).apply()
        _uiState.update { it.copy(perfilPublico = new) }
        persistToFirestore()
    }

    fun toggleResenasEnPerfil() {
        val new = !_uiState.value.resenasEnPerfil
        prefs.edit().putBoolean("resenas_en_perfil", new).apply()
        _uiState.update { it.copy(resenasEnPerfil = new) }
        persistToFirestore()
    }

    fun setLanguage(code: String) {
        if (code == _uiState.value.selectedLanguage) return
        LanguageManager.save(context, code)
        _uiState.update { it.copy(selectedLanguage = code, languageChanged = true) }
    }

    fun onLanguageChangeHandled() {
        _uiState.update { it.copy(languageChanged = false) }
    }
}