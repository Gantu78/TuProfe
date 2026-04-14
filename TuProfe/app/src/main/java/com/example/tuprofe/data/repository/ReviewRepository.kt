package com.example.tuprofe.data.repository

import com.example.tuprofe.data.ReviewInfo
import com.example.tuprofe.data.datasource.ResenaRemoteDataSource
import com.example.tuprofe.data.dtos.CreateReviewDto
import com.example.tuprofe.data.dtos.toReviewInfo
import retrofit2.HttpException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

class ReviewRepository @Inject constructor(
    private val reviewRemoteDataSource: ResenaRemoteDataSource
){

    suspend fun getReviews(): Result<List<ReviewInfo>> {
        return try {
            val reviews = reviewRemoteDataSource.getAllReviews().map { it.toReviewInfo() }
            Result.success(reviews)
        } catch (e: HttpException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getReviewById(reviewId: String): Result<ReviewInfo> {
        return try {
            val review = reviewRemoteDataSource.getReviewById(reviewId).toReviewInfo()
            Result.success(review)
        } catch (e: HttpException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserReviews(userId: String): Result<List<ReviewInfo>> {
        return try {
            val reviews = reviewRemoteDataSource.getUserReviews(userId).map { it.toReviewInfo() }
            Result.success(reviews)
        } catch (e: HttpException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createReview(userId: String, professorId: String, content: String, rating: Int): Result<Unit> {
        return try {
            val uId = userId.toIntOrNull() ?: 0
            val pId = professorId.toIntOrNull() ?: 0
            
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            val currentTime = sdf.format(Date())

            val createReviewDto = CreateReviewDto(
                userId = uId,
                professorId = pId,
                content = content,
                rating = rating,
                time = currentTime
            )
            reviewRemoteDataSource.createReview(createReviewDto)
            Result.success(Unit)
        } catch (e: HttpException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateReview(reviewId: String, content: String, rating: Int): Result<Unit> {
        return try {
            val updateReviewDto = CreateReviewDto(
                content = content,
                rating = rating
            )
            reviewRemoteDataSource.updateReview(reviewId, updateReviewDto)
            Result.success(Unit)
        } catch (e: HttpException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteReview(reviewId: String): Result<Unit> {
        return try {
            reviewRemoteDataSource.deleteReview(reviewId)
            Result.success(Unit)
        } catch (e: HttpException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
