package com.example.tuprofe.data.dtos

import com.example.tuprofe.data.CommentInfo
import com.example.tuprofe.data.Usuario
import java.text.SimpleDateFormat
import java.util.Locale

private fun formatCommentDate(raw: String?): String {
    if (raw.isNullOrEmpty()) return ""
    return try {
        val input = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        val output = SimpleDateFormat("d 'de' MMMM 'de' yyyy", Locale("es"))
        output.format(input.parse(raw)!!)
    } catch (e: Exception) {
        raw
    }
}

data class CommentDto(
    val id: String? = null,
    val reviewId: String? = null,
    val parentCommentId: String? = null,
    val userId: String? = null,
    val content: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val likesCount: Int = 0,
    var liked: Boolean = false,
    val repliesCount: Int = 0,
    val user: UserDto? = null
) {
    constructor() : this("", "", null, "", "", "", null, 0, false, 0, null)
}

fun CommentDto.toCommentInfo(): CommentInfo {
    val authorId = userId ?: user?.id ?: "0"
    val usuarioModel = if (user != null) {
        user.toUsuario().copy(usuarioId = authorId)
    } else {
        Usuario(
            usuarioId = authorId,
            nombreUsu = "Usuario $authorId",
            email = "",
            carrera = "",
            imageprofeUrl = null,
            followingCount = 0,
            followersCount = 0,
            followed = false
        )
    }
    return CommentInfo(
        commentId = id ?: "",
        reviewId = reviewId ?: "",
        parentCommentId = parentCommentId,
        usuario = usuarioModel,
        content = content ?: "",
        time = formatCommentDate(createdAt),
        likes = likesCount,
        liked = liked,
        repliesCount = repliesCount,
        editado = !updatedAt.isNullOrEmpty()
    )
}
