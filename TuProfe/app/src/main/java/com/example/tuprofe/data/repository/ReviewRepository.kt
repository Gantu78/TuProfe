package com.example.tuprofe.data.repository

import android.util.Log
import com.example.tuprofe.data.ReviewInfo
import com.example.tuprofe.data.datasource.AuthRemoteDataSource
import com.example.tuprofe.data.datasource.ProfessorRemoteDataSource
import com.example.tuprofe.data.datasource.ReviewRemoteDataSource
import com.example.tuprofe.data.datasource.UserRemoteDataSource
import com.example.tuprofe.data.dtos.CreateReviewDto
import com.example.tuprofe.data.dtos.CreateReviewProfessorDto
import com.example.tuprofe.data.dtos.CreateReviewUserDto
import com.example.tuprofe.data.dtos.toReviewInfo
import retrofit2.HttpException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject
import kotlin.String

class ReviewRepository @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val reviewRemoteDataSource: ReviewRemoteDataSource,
    private val userRemoteDataSource: UserRemoteDataSource,
    private val professorRemoteDataSource: ProfessorRemoteDataSource
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

    suspend fun getReviewById(reviewId: String, currentUserId: String = ""): Result<ReviewInfo> {
        return try {
            val review = reviewRemoteDataSource.getReviewById(reviewId, currentUserId).toReviewInfo()
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

    suspend fun createReview(userId: String, professorId: String, content: String, rating: Int, materia: String): Result<Unit> {
        return try {

            Log.d("ReviewRepo", "Buscando usuario: $userId")
            val user = userRemoteDataSource.getUserById(userId)
            Log.d("ReviewRepo", "Usuario encontrado: ${user.username}")

            Log.d("ReviewRepo", "Buscando profesor: $professorId")
            val prof = professorRemoteDataSource.getProfessorById(professorId)
            Log.d("ReviewRepo", "Profesor encontrado: ${prof.name}")

            val createReviewUserDto = CreateReviewUserDto(
                username = user.username
            )
            val createReviewProfessorDto = CreateReviewProfessorDto(
                name = prof.name,
                department = prof.department,
                foto = prof.foto_prof
            )

            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            val currentTime = sdf.format(Date())

            val createReviewDto = CreateReviewDto(
                userId = userId,
                professorId = professorId,
                content = content,
                rating = rating,
                time = currentTime,
                materia = materia,
                user =  createReviewUserDto,
                professor = createReviewProfessorDto
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
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            val currentTime = sdf.format(Date())

            val updateReviewDto = CreateReviewDto(
                content = content,
                rating = rating,
                time = currentTime
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

    suspend fun sendOrDeleteReviewLike(reviewId: String, userId: String): Result<Unit>{
        return try{
            reviewRemoteDataSource.SendOrDeleteReviewLike(reviewId, userId)
            Result.success(Unit)
        } catch (e: HttpException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
