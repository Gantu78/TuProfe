package com.example.tuprofe.navegation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object NotifDeepLinkManager {
    private val _pendingTarget = MutableStateFlow<String?>(null)
    val pendingTarget: StateFlow<String?> = _pendingTarget.asStateFlow()

    fun set(target: String) { _pendingTarget.value = target }
    fun clear() { _pendingTarget.value = null }
}
