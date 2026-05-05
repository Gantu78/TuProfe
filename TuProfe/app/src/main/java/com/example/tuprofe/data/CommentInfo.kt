package com.example.tuprofe.data

data class CommentInfo(
    val commentId: String,
    val reviewId: String,
    val parentCommentId: String? = null,
    val usuario: Usuario,
    val content: String,
    val time: String,
    val likes: Int = 0,
    val liked: Boolean = false,
    val repliesCount: Int = 0,
    val editado: Boolean = false
)
