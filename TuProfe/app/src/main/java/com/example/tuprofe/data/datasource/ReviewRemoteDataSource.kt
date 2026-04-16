package com.example.tuprofe.data.datasource

import com.example.tuprofe.data.dtos.CreateReviewDto
import com.example.tuprofe.data.dtos.ReviewDto

interface ReviewRemoteDataSource  {

    suspend fun getAllReviews(): List<ReviewDto>
    suspend fun getReviewById(id:String): ReviewDto

    suspend fun createReview(review: CreateReviewDto): Unit
    suspend fun deleteReview(id: String): Unit

    suspend fun updateReview(id: String, review: CreateReviewDto): Unit

    suspend fun getUserReviews(userId: String): List<ReviewDto>
}
