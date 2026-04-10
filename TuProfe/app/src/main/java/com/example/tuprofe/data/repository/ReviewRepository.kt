package com.example.tuprofe.data.repository

import coil.network.HttpException
import com.example.tuprofe.data.Profesor
import com.example.tuprofe.data.ReviewInfo
import com.example.tuprofe.data.datasource.ResenaRemoteDataSource
import com.example.tuprofe.data.dtos.CreateReviewDto
import com.example.tuprofe.data.dtos.toReviewInfo
import javax.inject.Inject

class ReviewRepository @Inject constructor(
    private val reviewRemoteDataSource: ResenaRemoteDataSource
){

    suspend fun getReviews(): Result<List<ReviewInfo>>{
        return try {
            val reviews = reviewRemoteDataSource.getAllReviews().map { it.toReviewInfo() }
            Result.success(reviews)
        } catch (e: HttpException){
            Result.failure(e)
        }

        catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun createReview(reviewId : String, profesor: Profesor, username: String, materiaId: String, content: String): Result<Unit> {
        return try{
        val createReviewDto =
            CreateReviewDto(profesor.toString(), username, materiaId, content)
            reviewRemoteDataSource.createReview(createReviewDto)
            Result.success(Unit)
        }catch (e: Exception){
            Result.failure(e)
        }
}


}