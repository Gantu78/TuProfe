package com.example.tuprofe.ui.chat

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.tuprofe.data.repository.AuthRepository
import com.example.tuprofe.data.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val authRepository: AuthRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val chatId: String = savedStateHandle["chatId"] ?: ""
    val otherUserId: String = savedStateHandle["otherUserId"] ?: ""

    private val _uiState = MutableStateFlow(ChatState())
    val uiState: StateFlow<ChatState> = _uiState.asStateFlow()

    val currentUserId: String
        get() = authRepository.currentUser?.uid ?: ""

    init {
        _uiState.update { it.copy(currentUserId = currentUserId) }
        viewModelScope.launch {
            val info = chatRepository.getOtherUserInfo(otherUserId).getOrNull()
            _uiState.update {
                it.copy(
                    otherUserName = info?.first ?: "",
                    otherUserImage = info?.second
                )
            }
            chatRepository.getOrCreateChat(chatId, listOf(currentUserId, otherUserId))
            listenMessages()
        }
    }

    private fun listenMessages() {
        viewModelScope.launch {
            chatRepository.listenMessages(chatId).collect { messages ->
                _uiState.update { it.copy(messages = messages, isLoading = false) }
            }
        }
        viewModelScope.launch {
            chatRepository.markRead(chatId, currentUserId)
        }
    }

    fun setPendingImage(uri: Uri?) {
        _uiState.update { it.copy(pendingImageUri = uri) }
    }

    fun sendMessage(text: String) {
        val trimmed = text.trim()
        val pendingUri = _uiState.value.pendingImageUri
        if (trimmed.isEmpty() && pendingUri == null) return

        _uiState.update { it.copy(isSending = true, pendingImageUri = null) }
        viewModelScope.launch {
            val imageUrl = if (pendingUri != null) {
                val result = chatRepository.uploadChatImage(currentUserId, pendingUri)
                result.onFailure { Log.e("ChatVM", "Image upload failed: ${it.message}", it) }
                result.getOrNull()
            } else null
            chatRepository.sendMessage(chatId, currentUserId, trimmed, imageUrl)
            _uiState.update { it.copy(isSending = false) }
        }
    }
}
