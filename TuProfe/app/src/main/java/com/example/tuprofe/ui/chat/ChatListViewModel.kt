package com.example.tuprofe.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tuprofe.data.ChatInfo
import com.example.tuprofe.data.repository.AuthRepository
import com.example.tuprofe.data.repository.ChatRepository
import com.example.tuprofe.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatListState())
    val uiState: StateFlow<ChatListState> = _uiState.asStateFlow()

    private val currentUserId: String
        get() = authRepository.currentUser?.uid ?: ""

    init {
        _uiState.update { it.copy(currentUserId = currentUserId) }
        listenChats()
    }

    private fun listenChats() {
        val uid = currentUserId
        if (uid.isEmpty()) return
        viewModelScope.launch {
            chatRepository.listenChats(uid).collect { rawChats ->
                val enriched = rawChats.map { chat ->
                    if (chat.otherUserName.isEmpty()) {
                        val info = chatRepository.getOtherUserInfo(chat.otherUserId).getOrNull()
                        chat.copy(
                            otherUserName = info?.first ?: chat.otherUserId,
                            otherUserImage = info?.second
                        )
                    } else {
                        chat
                    }
                }
                _uiState.update { it.copy(chats = enriched, isLoading = false) }
            }
        }
    }
}
