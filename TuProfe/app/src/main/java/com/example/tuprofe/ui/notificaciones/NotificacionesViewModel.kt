package com.example.tuprofe.ui.notificaciones

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import androidx.lifecycle.ViewModel
import javax.inject.Inject

@HiltViewModel
class NotificacionesViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val prefs = context.getSharedPreferences("notificaciones_prefs", Context.MODE_PRIVATE)

    private val _uiState = MutableStateFlow(
        NotificacionesState(
            likesEnabled = prefs.getBoolean("likes_enabled", true),
            comentariosEnabled = prefs.getBoolean("comentarios_enabled", true),
            seguidoresEnabled = prefs.getBoolean("seguidores_enabled", true),
            permissionGranted = hasNotificationPermission()
        )
    )
    val uiState: StateFlow<NotificacionesState> = _uiState.asStateFlow()

    fun toggleLikes() {
        val new = !_uiState.value.likesEnabled
        prefs.edit().putBoolean("likes_enabled", new).apply()
        _uiState.update { it.copy(likesEnabled = new) }
    }

    fun toggleComentarios() {
        val new = !_uiState.value.comentariosEnabled
        prefs.edit().putBoolean("comentarios_enabled", new).apply()
        _uiState.update { it.copy(comentariosEnabled = new) }
    }

    fun toggleSeguidores() {
        val new = !_uiState.value.seguidoresEnabled
        prefs.edit().putBoolean("seguidores_enabled", new).apply()
        _uiState.update { it.copy(seguidoresEnabled = new) }
    }

    fun onPermissionResult(granted: Boolean) {
        _uiState.update { it.copy(permissionGranted = granted) }
    }

    fun refreshPermissionState() {
        _uiState.update { it.copy(permissionGranted = hasNotificationPermission()) }
    }

    private fun hasNotificationPermission(): Boolean {
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }
}
