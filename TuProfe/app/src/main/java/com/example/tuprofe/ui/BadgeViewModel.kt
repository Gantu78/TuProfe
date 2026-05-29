package com.example.tuprofe.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tuprofe.data.repository.AuthRepository
import com.example.tuprofe.data.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BadgeViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _hasUnreadChats = MutableStateFlow(false)
    val hasUnreadChats: StateFlow<Boolean> = _hasUnreadChats.asStateFlow()

    init {
        val uid = authRepository.currentUser?.uid
        if (!uid.isNullOrEmpty()) {
            viewModelScope.launch {
                chatRepository.listenChats(uid).collect { chats ->
                    _hasUnreadChats.value = chats.any { it.unreadCount > 0 }
                }
            }
        }
    }
}
