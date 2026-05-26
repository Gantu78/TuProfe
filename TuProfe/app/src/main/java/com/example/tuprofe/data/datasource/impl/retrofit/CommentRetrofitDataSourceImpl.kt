package com.example.tuprofe.data.datasource.impl.retrofit

import com.example.tuprofe.data.datasource.CommentRemoteDataSource
import com.example.tuprofe.data.datasource.services.CommentRetrofitService
import com.example.tuprofe.data.dtos.CommentDto
import com.example.tuprofe.data.dtos.CreateCommentDto
import javax.inject.Inject

class CommentRetrofitDataSourceImpl @Inject constructor(
    private val service: CommentRetrofitService
) : CommentRemoteDataSource {

    override suspend fun getCommentsByReviewId(reviewId: String, currentUserId: String): List<CommentDto> {
        return service.getCommentsByReview(reviewId, currentUserId.ifEmpty { null })
    }

    override suspend fun getCommentById(commentId: String, currentUserId: String): CommentDto {
        return service.getCommentById(commentId, currentUserId.ifEmpty { null })
    }

    override suspend fun getRepliesByCommentId(parentCommentId: String, currentUserId: String): List<CommentDto> {
        return service.getReplies(parentCommentId, currentUserId.ifEmpty { null })
    }

    override suspend fun createComment(comment: CreateCommentDto): String {
        val response = service.createComment(comment)
        return response["id"] ?: ""
    }

    override suspend fun updateComment(commentId: String, content: String) {
        service.updateComment(commentId, mapOf("content" to content))
    }

    override suspend fun deleteComment(commentId: String) {
        service.deleteComment(commentId)
    }

    override suspend fun sendOrDeleteCommentLike(commentId: String, userId: String) {
        service.toggleLike(commentId, mapOf("userId" to userId))
    }

    override suspend fun getUserComments(userId: String): List<CommentDto> {
        return service.getUserComments(userId)
    }
}
