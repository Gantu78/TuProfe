package com.example.tuprofe.data.datasource.impl.retrofit

import com.example.tuprofe.data.datasource.ReviewRemoteDataSource
import com.example.tuprofe.data.datasource.Services.ReviewRetrofitService
import com.example.tuprofe.data.dtos.CreateReviewDto
import com.example.tuprofe.data.dtos.ReviewDto
import javax.inject.Inject

class ReviewRetrofitDataSourceImpl @Inject constructor(
    val service: ReviewRetrofitService
) : ReviewRemoteDataSource {


    override suspend fun getAllReviews(): List<ReviewDto>{
         return service.getAllReviews()
    }

    override suspend fun getReviewById(id:String): ReviewDto{
        return service.getReviewById(id)
    }

    override suspend fun createReview(review: CreateReviewDto) {
        review.userId = "1"
        service.createReview(review)
    }

    override suspend fun deleteReview(id: String){
        service.deleteReview(id.toInt())
    }

    override suspend fun updateReview(id: String, review: CreateReviewDto){
        service.updateReview(id.toInt(),review)
    }

    override suspend fun getUserReviews(userId: String): List<ReviewDto> {
        return service.getUserReviews(userId.toInt())
    }
}
