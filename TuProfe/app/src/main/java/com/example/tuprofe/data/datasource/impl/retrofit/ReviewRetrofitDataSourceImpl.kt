package com.example.tuprofe.data.datasource.impl.retrofit

import com.example.tuprofe.data.datasource.ReviewRemoteDataSource
import com.example.tuprofe.data.datasource.services.ReviewRetrofitService
import com.example.tuprofe.data.dtos.CreateReviewDto
import com.example.tuprofe.data.dtos.ReviewDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ReviewRetrofitDataSourceImpl @Inject constructor(
    val service: ReviewRetrofitService
) : ReviewRemoteDataSource {

    override suspend fun getAllReviews(): List<ReviewDto> {
        return service.getAllReviews()
    }

    override suspend fun getReviewById(id: String, currentUserId: String): ReviewDto {
        return service.getReviewById(id, currentUserId.ifEmpty { null })
    }

    override suspend fun createReview(review: CreateReviewDto) {
        // userId lo asigna el Repository desde el currentUser de Firebase Auth
        service.createReview(review)
    }

    override suspend fun deleteReview(id: String) {
        service.deleteReview(id.toInt())
    }

    override suspend fun updateReview(id: String, review: CreateReviewDto) {
        service.updateReview(id.toInt(), review)
    }

    override suspend fun getUserReviews(userId: String): List<ReviewDto> {
        return service.getUserReviews(userId)
    }

    override suspend fun SendOrDeleteReviewLike(reviewId: String, userId: String) {
        service.toggleLike(reviewId, mapOf("userId" to userId))
    }

    /**
     * Express no tiene sockets en tiempo real.
     * Emitimos una sola vez al suscribirse (equivalent a un fetch).
     * Para verdadero tiempo real, considera añadir SSE o WebSockets al servidor.
     */
    override suspend fun listenAllReviews(): Flow<List<ReviewDto>> = flow {
        emit(service.getAllReviews())
    }

    override suspend fun getMapMarkers(
        stars: Set<Int>,
        profesorNombres: Set<String>,
        materias: Set<String>
    ): List<ReviewDto> = getAllReviews().filter { dto ->
        dto.latitude != null && dto.longitude != null &&
        (stars.isEmpty() || dto.rating in stars) &&
        (profesorNombres.isEmpty() || dto.professor?.name in profesorNombres) &&
        (materias.isEmpty() || dto.materia in materias)
    }
}
