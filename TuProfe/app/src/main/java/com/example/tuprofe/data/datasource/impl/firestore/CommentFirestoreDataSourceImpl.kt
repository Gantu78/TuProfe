package com.example.tuprofe.data.datasource.impl.firestore

import com.example.tuprofe.data.datasource.CommentRemoteDataSource
import com.example.tuprofe.data.dtos.CommentDto
import com.example.tuprofe.data.dtos.CreateCommentDto
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CommentFirestoreDataSourceImpl @Inject constructor(
    private val db: FirebaseFirestore
) : CommentRemoteDataSource {

    override suspend fun getCommentsByReviewId(reviewId: String, currentUserId: String): List<CommentDto> {
        val snapshot = db.collection("comments")
            .whereEqualTo("reviewId", reviewId)
            .get().await()

        return snapshot.documents.mapNotNull { doc ->
            val dto = doc.toObject(CommentDto::class.java) ?: return@mapNotNull null
            val withId = dto.copy(id = doc.id)
            // Only top-level comments (no parent)
            if (withId.parentCommentId != null) return@mapNotNull null
            if (currentUserId.isNotEmpty()) {
                val likeDoc = db.collection("comments").document(doc.id)
                    .collection("likes").document(currentUserId).get().await()
                if (likeDoc.exists()) withId.copy(liked = true) else withId
            } else withId
        }
    }

    override suspend fun getCommentById(commentId: String, currentUserId: String): CommentDto {
        val doc = db.collection("comments").document(commentId).get().await()
        val dto = doc.toObject(CommentDto::class.java) ?: throw Exception("Comment not found")
        val withId = dto.copy(id = doc.id)
        return if (currentUserId.isNotEmpty()) {
            val likeDoc = db.collection("comments").document(commentId)
                .collection("likes").document(currentUserId).get().await()
            if (likeDoc.exists()) withId.copy(liked = true) else withId
        } else withId
    }

    override suspend fun getRepliesByCommentId(parentCommentId: String, currentUserId: String): List<CommentDto> {
        val snapshot = db.collection("comments")
            .whereEqualTo("parentCommentId", parentCommentId)
            .get().await()

        return snapshot.documents.mapNotNull { doc ->
            val dto = doc.toObject(CommentDto::class.java) ?: return@mapNotNull null
            val withId = dto.copy(id = doc.id)
            if (currentUserId.isNotEmpty()) {
                val likeDoc = db.collection("comments").document(doc.id)
                    .collection("likes").document(currentUserId).get().await()
                if (likeDoc.exists()) withId.copy(liked = true) else withId
            } else withId
        }
    }

    override suspend fun createComment(comment: CreateCommentDto): String {
        val data = hashMapOf(
            "reviewId" to comment.reviewId,
            "parentCommentId" to comment.parentCommentId,
            "userId" to comment.userId,
            "content" to comment.content,
            "createdAt" to comment.createdAt,
            "likesCount" to 0,
            "repliesCount" to 0,
            "user" to mapOf(
                "id" to comment.user?.id,
                "username" to comment.user?.username,
                "foto" to comment.user?.foto
            )
        )

        val docRef = db.collection("comments").add(data).await()

        if (comment.parentCommentId == null) {
            db.collection("reviews").document(comment.reviewId)
                .update("comment", FieldValue.increment(1)).await()
        } else {
            db.collection("comments").document(comment.parentCommentId)
                .update("repliesCount", FieldValue.increment(1)).await()
        }

        return docRef.id
    }

    override suspend fun deleteComment(commentId: String) {
        val doc = db.collection("comments").document(commentId).get().await()
        val dto = doc.toObject(CommentDto::class.java) ?: return

        db.collection("comments").document(commentId).delete().await()

        if (dto.parentCommentId == null) {
            val rId = dto.reviewId ?: return
            db.collection("reviews").document(rId)
                .update("comment", FieldValue.increment(-1)).await()
        } else {
            db.collection("comments").document(dto.parentCommentId!!)
                .update("repliesCount", FieldValue.increment(-1)).await()
        }
    }

    override suspend fun updateComment(commentId: String, content: String) {
        db.collection("comments").document(commentId)
            .update(
                mapOf(
                    "content" to content,
                    "updatedAt" to java.time.Instant.now().toString()
                )
            ).await()
    }

    override suspend fun sendOrDeleteCommentLike(commentId: String, userId: String) {
        val commentRef = db.collection("comments").document(commentId)
        val likeRef = commentRef.collection("likes").document(userId)

        db.runTransaction { transaction ->
            val likeDoc = transaction.get(likeRef)
            if (likeDoc.exists()) {
                transaction.delete(likeRef)
                transaction.update(commentRef, "likesCount", FieldValue.increment(-1))
            } else {
                transaction.set(likeRef, mapOf("timestamp" to FieldValue.serverTimestamp()))
                transaction.update(commentRef, "likesCount", FieldValue.increment(1))
            }
        }.await()
    }

    override suspend fun getUserComments(userId: String): List<CommentDto> {
        val snapshot = db.collection("comments")
            .whereEqualTo("userId", userId)
            .get().await()

        return snapshot.documents.mapNotNull { doc ->
            val dto = doc.toObject(CommentDto::class.java) ?: return@mapNotNull null
            dto.copy(id = doc.id)
        }
    }
}
