package com.example.tuprofe.data.dtos

data class CreateCommentDto(
    val reviewId: String,
    val parentCommentId: String? = null,
    val userId: String,
    val content: String,
    val createdAt: String = "",
    val user: UserDto? = null
)
