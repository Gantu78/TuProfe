package com.example.tuprofe.data.datasource

import com.example.tuprofe.data.dtos.CommentDto
import com.example.tuprofe.data.dtos.CreateCommentDto

interface CommentRemoteDataSource {
    suspend fun getCommentsByReviewId(reviewId: String, currentUserId: String): List<CommentDto>
    suspend fun getCommentById(commentId: String, currentUserId: String): CommentDto
    suspend fun getRepliesByCommentId(parentCommentId: String, currentUserId: String): List<CommentDto>
    suspend fun createComment(comment: CreateCommentDto): String
    suspend fun deleteComment(commentId: String)
    suspend fun updateComment(commentId: String, content: String)
    suspend fun sendOrDeleteCommentLike(commentId: String, userId: String)
    suspend fun getUserComments(userId: String): List<CommentDto>
}
