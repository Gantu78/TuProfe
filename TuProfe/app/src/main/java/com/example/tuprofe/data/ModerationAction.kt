package com.example.tuprofe.data

sealed class ModerationAction {
    data class Report(val targetId: String, val targetType: String, val authorId: String) : ModerationAction()
    data class Block(val authorId: String, val authorName: String) : ModerationAction()
    data class Mute(val targetId: String, val targetType: String) : ModerationAction()
}
