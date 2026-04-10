package com.example.tuprofe.data.datasource

import com.example.tuprofe.data.ReviewInfo
import com.example.tuprofe.data.dtos.CreateReviewDto
import com.example.tuprofe.data.dtos.ResenaDto

interface ResenaRemoteDataSource  {

    suspend fun getAllReviews(): List<ResenaDto>
    suspend fun getReviewById(id:String): ResenaDto

    suspend fun createReview(review: CreateReviewDto): Unit
    suspend fun deleteReview(id: String): Unit

    suspend fun updateReview(id: String, review: CreateReviewDto): Unit


}