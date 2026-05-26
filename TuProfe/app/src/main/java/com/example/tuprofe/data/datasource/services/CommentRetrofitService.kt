package com.example.tuprofe.data.datasource.services

import com.example.tuprofe.data.dtos.CommentDto
import com.example.tuprofe.data.dtos.CreateCommentDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface CommentRetrofitService {

    /** Comentarios raíz de una reseña */
    @GET("reviews/{reviewId}/comments")
    suspend fun getCommentsByReview(
        @Path("reviewId") reviewId: String,
        @Query("currentUserId") currentUserId: String? = null
    ): List<CommentDto>

    @GET("comments/{id}")
    suspend fun getCommentById(
        @Path("id") id: String,
        @Query("currentUserId") currentUserId: String? = null
    ): CommentDto

    /** Respuestas a un comentario */
    @GET("comments/{id}/replies")
    suspend fun getReplies(
        @Path("id") parentCommentId: String,
        @Query("currentUserId") currentUserId: String? = null
    ): List<CommentDto>

    @POST("comments")
    suspend fun createComment(@Body comment: CreateCommentDto): Map<String, String>

    @PUT("comments/{id}")
    suspend fun updateComment(
        @Path("id") id: String,
        @Body body: Map<String, String>
    ): CommentDto

    @DELETE("comments/{id}")
    suspend fun deleteComment(@Path("id") id: String)

    /** Activa o desactiva el like. Body: { "userId": "..." } */
    @POST("comments/{id}/like-toggle")
    suspend fun toggleLike(
        @Path("id") id: String,
        @Body body: Map<String, String>
    ): Map<String, Any>

    @GET("users/{userId}/comments")
    suspend fun getUserComments(@Path("userId") userId: String): List<CommentDto>
}
