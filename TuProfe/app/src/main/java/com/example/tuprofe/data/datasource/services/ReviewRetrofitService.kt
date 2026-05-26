package com.example.tuprofe.data.datasource.services

import com.example.tuprofe.data.dtos.CreateReviewDto
import com.example.tuprofe.data.dtos.ReviewDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ReviewRetrofitService {

    @GET("reviews")
    suspend fun getAllReviews(@Query("currentUserId") currentUserId: String? = null): List<ReviewDto>

    @GET("reviews/{id}")
    suspend fun getReviewById(
        @Path("id") id: String,
        @Query("currentUserId") currentUserId: String? = null
    ): ReviewDto

    @POST("reviews")
    suspend fun createReview(@Body review: CreateReviewDto): ReviewDto

    @PUT("reviews/{id}")
    suspend fun updateReview(@Path("id") id: Int, @Body review: CreateReviewDto): ReviewDto

    @DELETE("reviews/{id}")
    suspend fun deleteReview(@Path("id") id: Int)

    @GET("users/{id}/reviews")
    suspend fun getUserReviews(
        @Path("id") id: String,
        @Query("currentUserId") currentUserId: String? = null
    ): List<ReviewDto>

    /** Activa o desactiva el like. Body: { "userId": "..." } */
    @POST("reviews/{id}/like-toggle")
    suspend fun toggleLike(
        @Path("id") id: String,
        @Body body: Map<String, String>
    ): Map<String, Any>

}
