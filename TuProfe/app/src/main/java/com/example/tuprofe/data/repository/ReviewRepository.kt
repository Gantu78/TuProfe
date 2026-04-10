package com.example.tuprofe.data.repository

import coil.network.HttpException
import com.example.tuprofe.data.ReviewInfo
import com.example.tuprofe.data.datasource.ResenaRemoteDataSource
import com.example.tuprofe.data.dtos.CreateReviewDto
import com.example.tuprofe.data.dtos.toReviewInfo
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
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

    suspend fun createReview(userId: Int, professorId: Int, content: String, rating: Int): Result<Unit> {
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            val currentTime = sdf.format(Date())

            val createReviewDto = CreateReviewDto(
                userId = userId,
                professorId = professorId,
                content = content,
                rating = rating,
                time = currentTime
            )
            reviewRemoteDataSource.createReview(createReviewDto)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
