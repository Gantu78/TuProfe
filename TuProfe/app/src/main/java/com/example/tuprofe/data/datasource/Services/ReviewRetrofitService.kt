package com.example.tuprofe.data.datasource.Services

import com.example.tuprofe.data.dtos.CreateReviewDto
import com.example.tuprofe.data.dtos.ProfessorDto
import com.example.tuprofe.data.dtos.ReviewDto
import com.example.tuprofe.data.dtos.UserDto
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
    suspend fun createReview(@Body review: CreateReviewDto): Unit

    @DELETE("reviews/{id}")
    suspend fun deleteReview(@Path("id") id: Int): Unit

    @PUT("reviews/{id}")
    suspend fun updateReview(@Path("id") id: Int, @Body review: CreateReviewDto): Unit

    @GET("reviews/{id}")
    suspend fun getReviewById(@Path("id") id: String): ReviewDto

    @GET("professors/{id}")
    suspend fun getProfessorById(@Path("id") id: Int): ProfessorDto

    @GET("professors")
    suspend fun getAllProfessors(): List<ProfessorDto>

    @GET("users/{id}/reviews")
    suspend fun getUserReviews(@Path("id") id: Int): List<ReviewDto>

    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: Int): UserDto
}
