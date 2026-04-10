package com.example.tuprofe.data.datasource.Services

import com.example.tuprofe.data.dtos.CreateReviewDto
import com.example.tuprofe.data.dtos.ResenaDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ReviewRetrofitService {

    @GET("reviews")
    suspend fun getAllReviews(): List<ResenaDto>

    @POST("reviews")
    suspend fun createReview(@Body   Review: CreateReviewDto ) : Unit


    @DELETE ("reviews/{id}")
    suspend fun deleteReview(@Path("id") id:Int):Unit

    @PUT("reviews/{id}")
    suspend fun updateTweet(@Path("id") id:Int, @Body review: CreateReviewDto): Unit

    @GET("reviews/{id}")
    suspend fun getReviewById(@Path("id") id: String): ResenaDto


}