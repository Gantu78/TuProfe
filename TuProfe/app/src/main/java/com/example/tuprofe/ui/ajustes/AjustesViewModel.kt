package com.example.tuprofe.ui.ajustes

import android.content.Context
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AjustesViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val prefs = context.getSharedPreferences("ajustes_privacidad_prefs", Context.MODE_PRIVATE)

    private val _uiState = MutableStateFlow(
        AjustesState(
            perfilAnonimo = prefs.getBoolean("perfil_anonimo", false),
            perfilPublico = prefs.getBoolean("perfil_publico", true),
            resenasEnPerfil = prefs.getBoolean("resenas_en_perfil", true)
        )
    )
    val uiState: StateFlow<AjustesState> = _uiState.asStateFlow()

    fun togglePerfilAnonimo() {
        val new = !_uiState.value.perfilAnonimo
        prefs.edit().putBoolean("perfil_anonimo", new).apply()
        _uiState.update { it.copy(perfilAnonimo = new) }
    }

    fun togglePerfilPublico() {
        val new = !_uiState.value.perfilPublico
        prefs.edit().putBoolean("perfil_publico", new).apply()
        _uiState.update { it.copy(perfilPublico = new) }
    }

    fun toggleResenasEnPerfil() {
        val new = !_uiState.value.resenasEnPerfil
        prefs.edit().putBoolean("resenas_en_perfil", new).apply()
        _uiState.update { it.copy(resenasEnPerfil = new) }
    }
}
