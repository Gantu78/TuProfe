package com.example.tuprofe.data.datasource.impl

import com.example.tuprofe.data.ReviewInfo
import com.example.tuprofe.data.datasource.ResenaRemoteDataSource
import com.example.tuprofe.data.datasource.Services.ReviewRetrofitService
import com.example.tuprofe.data.dtos.CreateReviewDto
import com.example.tuprofe.data.dtos.ResenaDto

import retrofit2.http.Path
import javax.inject.Inject

class ReviewRetrofitDataSourceImpl @Inject constructor(
    val service: ReviewRetrofitService
) : ResenaRemoteDataSource {


    override suspend fun getAllReviews(): List<ResenaDto>{
         return service.getAllReviews()
    }

    override suspend fun getReviewById(id:String): ResenaDto{
        return service.getReviewById(id)
    }

    override suspend fun createReview(review: CreateReviewDto) {
        service.createReview(review)
    }

    override suspend fun deleteReview(id: String){
        service.deleteReview(id.toInt())
    }

    override suspend fun updateReview(id: String, review: CreateReviewDto){
        service.updateTweet(id.toInt(),review)
    }


}