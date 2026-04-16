package com.example.tuprofe.data.datasource.impl.firestore

import com.example.tuprofe.data.datasource.ReviewRemoteDataSource
import com.example.tuprofe.data.dtos.CreateReviewDto
import com.example.tuprofe.data.dtos.ReviewDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ReviewFirestoreDataSourceImpl @Inject constructor(
    private val db: FirebaseFirestore
): ReviewRemoteDataSource {
    override suspend fun getAllReviews(): List<ReviewDto> {
        return db.collection("reviews").get().await().documents.map { doc ->
            val dto = doc.toObject(ReviewDto::class.java) ?: ReviewDto()
            dto.copy(id = doc.id)
        }
    }

    override suspend fun getReviewById(id: String): ReviewDto {
        return db.collection("reviews").document(id).get().await().toObject(ReviewDto::class.java)?: throw Exception("Review not found")
    }

    override suspend fun createReview(review: CreateReviewDto) {
        db.collection("reviews").add(review).await()
    }

    override suspend fun deleteReview(id: String) {
        db.collection("reviews").document(id).delete().await()
    }

    override suspend fun updateReview(id: String, review: CreateReviewDto) {
        val updates = mutableMapOf<String, Any?>()
        review.content?.let { updates["content"] = it }
        review.rating?.let { updates["rating"] = it }
        review.time?.let { updates["time"] = it }
        
        if (updates.isNotEmpty()) {
            db.collection("reviews").document(id).update(updates).await()
        }
    }

    override suspend fun getUserReviews(userId: String): List<ReviewDto> {
        val snapshot = db.collection("reviews").whereEqualTo("userId",userId).get().await()
        return snapshot.map {doc ->
            val review = doc.toObject(ReviewDto::class.java)
            review?.copy(id=doc.id)?:throw Exception ("review not found")
        }
    }
}
