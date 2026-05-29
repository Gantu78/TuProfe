package com.example.tuprofe.ui.notificaciones

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tuprofe.data.AppNotification
import com.example.tuprofe.data.repository.AuthRepository
import com.example.tuprofe.data.repository.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotifInboxViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _notifications = MutableStateFlow<List<AppNotification>>(emptyList())
    val notifications: StateFlow<List<AppNotification>> = _notifications.asStateFlow()

    private val uid: String? get() = authRepository.currentUser?.uid

    init {
        uid?.let { userId ->
            viewModelScope.launch {
                notificationRepository.listenNotifications(userId).collect { list ->
                    _notifications.value = list
                }
            }
        }
    }

    fun onNotificationClick(notif: AppNotification) {
        val userId = uid ?: return
        if (!notif.isRead) {
            viewModelScope.launch {
                notificationRepository.markRead(userId, notif.id)
            }
        }
    }

    fun markAllRead() {
        val userId = uid ?: return
        viewModelScope.launch {
            notificationRepository.markAllRead(userId)
        }
    }
}