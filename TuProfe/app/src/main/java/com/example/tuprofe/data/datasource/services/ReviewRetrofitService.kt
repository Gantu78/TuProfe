package com.example.tuprofe.data.datasource.services

import com.example.tuprofe.data.dtos.CreateReviewDto
import com.example.tuprofe.data.dtos.ReviewDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ReviewRetrofitService {

    @GET("reviews")
    suspend fun getAllReviews(): List<ReviewDto>

    @POST("reviews")
    suspend fun createReview(@Body review: CreateReviewDto)

    @DELETE("reviews/{id}")
    suspend fun deleteReview(@Path("id") id: Int)

    @PUT("reviews/{id}")
    suspend fun updateReview(@Path("id") id: Int, @Body review: CreateReviewDto)

    @GET("reviews/{id}")
    suspend fun getReviewById(@Path("id") id: String): ReviewDto

    @GET("users/{id}/reviews")
    suspend fun getUserReviews(@Path("id") id: Int): List<ReviewDto>

}
