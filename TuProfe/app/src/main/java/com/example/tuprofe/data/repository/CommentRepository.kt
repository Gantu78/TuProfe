package com.example.tuprofe.data.repository

import com.example.tuprofe.data.CommentInfo
import com.example.tuprofe.data.datasource.CommentRemoteDataSource
import com.example.tuprofe.data.datasource.UserRemoteDataSource
import com.example.tuprofe.data.dtos.CreateCommentDto
import com.example.tuprofe.data.dtos.UserDto
import com.example.tuprofe.data.dtos.toCommentInfo
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

class CommentRepository @Inject constructor(
    private val commentRemoteDataSource: CommentRemoteDataSource,
    private val userRemoteDataSource: UserRemoteDataSource
) {

    suspend fun getCommentsByReviewId(reviewId: String, currentUserId: String): Result<List<CommentInfo>> {
        return try {
            val comments = commentRemoteDataSource.getCommentsByReviewId(reviewId, currentUserId)
                .map { it.toCommentInfo() }
                .sortedByDescending { it.likes }
            Result.success(comments)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCommentById(commentId: String, currentUserId: String): Result<CommentInfo> {
        return try {
            val comment = commentRemoteDataSource.getCommentById(commentId, currentUserId).toCommentInfo()
            Result.success(comment)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getRepliesByCommentId(parentCommentId: String, currentUserId: String): Result<List<CommentInfo>> {
        return try {
            val replies = commentRemoteDataSource.getRepliesByCommentId(parentCommentId, currentUserId)
                .map { it.toCommentInfo() }
            Result.success(replies)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createComment(
        reviewId: String,
        parentCommentId: String?,
        userId: String,
        content: String
    ): Result<String> {
        return try {
            val user = userRemoteDataSource.getUserById(userId)
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            val currentTime = sdf.format(Date())

            val userDto = UserDto(
                id = userId,
                username = user.username,
                foto = user.foto
            )

            val dto = CreateCommentDto(
                reviewId = reviewId,
                parentCommentId = parentCommentId,
                userId = userId,
                content = content,
                createdAt = currentTime,
                user = userDto
            )
            val newId = commentRemoteDataSource.createComment(dto)
            Result.success(newId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteComment(commentId: String): Result<Unit> {
        return try {
            commentRemoteDataSource.deleteComment(commentId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateComment(commentId: String, content: String): Result<Unit> {
        return try {
            commentRemoteDataSource.updateComment(commentId, content)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun sendOrDeleteCommentLike(commentId: String, userId: String): Result<Unit> {
        return try {
            commentRemoteDataSource.sendOrDeleteCommentLike(commentId, userId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserComments(userId: String): Result<List<CommentInfo>> {
        return try {
            val comments = commentRemoteDataSource.getUserComments(userId)
                .map { it.toCommentInfo() }
            Result.success(comments)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
