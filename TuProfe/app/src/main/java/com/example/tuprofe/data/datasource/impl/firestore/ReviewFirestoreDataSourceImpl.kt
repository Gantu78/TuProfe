package com.example.tuprofe.data.datasource.impl.firestore

import com.example.tuprofe.data.datasource.ReviewRemoteDataSource
import com.example.tuprofe.data.dtos.CreateReviewDto
import com.example.tuprofe.data.dtos.ReviewDto
import com.google.firebase.firestore.FieldValue
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

    override suspend fun getReviewById(
        id: String,
        currentUserId: String
    ): ReviewDto {
        val tweetRef = db.collection("reviews").document(id)
        val tweetSnapshot = tweetRef.get().await()
        val tweet = tweetSnapshot.toObject(ReviewDto::class.java) ?: throw Exception("Review not found")

        if(currentUserId.isNotEmpty()){
            val likeSnapshot = tweetRef.collection("likes").document(currentUserId).get().await()
            val hasliked = likeSnapshot.exists()

            if(hasliked){
                tweet.liked = true
            }
        }
        return tweet
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
        updates["updatedAt"] = java.time.Instant.now().toString()

        db.collection("reviews").document(id).update(updates).await()
    }

    override suspend fun getUserReviews(userId: String): List<ReviewDto> {
        val snapshot = db.collection("reviews").whereEqualTo("userId",userId).get().await()
        return snapshot.map {doc ->
            val review = doc.toObject(ReviewDto::class.java)
            review?.copy(id=doc.id)?:throw Exception ("review not found")
        }
    }

    override suspend fun SendOrDeleteReviewLike(reviewId: String, userId: String) {
        val reviewRef = db.collection("reviews").document(reviewId)
        val likesRef = reviewRef.collection("likes").document(userId)

        db.runTransaction { transaction ->

            val likeDoc = transaction.get(likesRef)
            if(likeDoc.exists()){

                transaction.delete(likesRef)
                transaction.update(reviewRef, "likesCount", FieldValue.increment(-1))
            }else{
                transaction.set(likesRef, mapOf("timestamp" to FieldValue.serverTimestamp()))
                transaction.update(reviewRef, "likesCount", FieldValue.increment(1))
            }


        }
    }
}
