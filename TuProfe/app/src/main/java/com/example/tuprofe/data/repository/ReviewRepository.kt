package com.example.tuprofe.data.repository

import coil.network.HttpException
import com.example.tuprofe.data.ReviewInfo
import com.example.tuprofe.data.datasource.ResenaRemoteDataSource
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
    
}
