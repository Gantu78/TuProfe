package com.example.tuprofe.data.datasource

import com.example.tuprofe.data.dtos.CreateReviewDto
import com.example.tuprofe.data.dtos.ReviewDto

interface ReviewRemoteDataSource  {

    suspend fun getAllReviews(): List<ReviewDto>
    suspend fun getReviewById(id:String, currentUserId: String = ""): ReviewDto

    suspend fun createReview(review: CreateReviewDto)
    suspend fun deleteReview(id: String)

    suspend fun updateReview(id: String, review: CreateReviewDto)

    suspend fun getUserReviews(userId: String): List<ReviewDto>

    suspend fun SendOrDeleteReviewLike(reviewId: String, userId: String)
}
