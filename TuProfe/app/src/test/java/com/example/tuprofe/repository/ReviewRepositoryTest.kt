package com.example.tuprofe.repository

import android.util.Log
import com.example.tuprofe.data.datasource.AuthRemoteDataSource
import com.example.tuprofe.data.datasource.ProfessorRemoteDataSource
import com.example.tuprofe.data.datasource.ReviewRemoteDataSource
import com.example.tuprofe.data.datasource.UserRemoteDataSource
import com.example.tuprofe.data.dtos.ProfessorDto
import com.example.tuprofe.data.dtos.ReviewDto
import com.example.tuprofe.data.dtos.UserDto
import com.example.tuprofe.data.repository.ReviewRepository
import com.google.common.truth.Truth
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ReviewRepositoryTest {

    private val authDataSource = mockk<AuthRemoteDataSource>()
    private val reviewDataSource = mockk<ReviewRemoteDataSource>()
    private val userDataSource = mockk<UserRemoteDataSource>()
    private val professorDataSource = mockk<ProfessorRemoteDataSource>()
    private val repository = ReviewRepository(
    authDataSource,
    reviewDataSource,
    userDataSource,
    professorDataSource
    )

    @Before
    fun setup() {
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
    }

    @Test
    fun `getReviews retorna una lista de ReviewInfo correctamente mapeada`() = runTest {
        val reviewDto = ReviewDto(
            id = "rev1",
            content = "Excelente profesor",
            rating = 5,
            userId = "user123",
            professorId = "prof456",
            likesCount = 0
        )
        coEvery { reviewDataSource.getAllReviews() } returns listOf(reviewDto)

        val result = repository.getReviews()

        Truth.assertThat(result.isSuccess).isTrue()
        val reviews = result.getOrNull()
        Truth.assertThat(reviews).hasSize(1)
        Truth.assertThat(reviews?.first()?.content).isEqualTo("Excelente profesor")
    }

    @Test
    fun `getReviewById con ID invalido retorna failure`() = runTest {
        val reviewId = "non_existent"
        coEvery { reviewDataSource.getReviewById(reviewId, "") } throws Exception("Review not found")

        val result = repository.getReviewById(reviewId)

        Truth.assertThat(result.isFailure).isTrue()
        Truth.assertThat(result.exceptionOrNull()?.message).isEqualTo("Review not found")
    }

    @Test
    fun `createReview exitoso llama al datasource despues de obtener usuario y profesor`() = runTest {
        // Arrange
        val userId = "user1"
        val profId = "prof1"
        val userDto = UserDto(id = userId, username = "Estudiante1")
        val profDto = ProfessorDto(
            id = profId,
            name = "Profesor X",
            department = "Ingeniería",
            subjects = listOf("Programación del papu"),
            foto_prof = null,
            createdAt = "",
            updatedAt = ""
        )


        coEvery { userDataSource.getUserById(userId, "") } returns userDto
        coEvery { professorDataSource.getProfessorById(profId) } returns profDto
        coEvery { reviewDataSource.createReview(any()) } returns Unit

        // Act
        val result = repository.createReview(userId, profId, "Buena clase", 5, "Programación del papu")

        // Assert
        Truth.assertThat(result.isSuccess).isTrue()
        coVerify { reviewDataSource.createReview(match {
            it.userId == userId && it.professorId == profId && it.content == "Buena clase"
        }) }
    }

    @Test
    fun `createReview retorna failure si el usuario no existe`() = runTest {
        val userId = "unknown"
        coEvery { userDataSource.getUserById(userId, "") } returns null

        val result = repository.createReview(userId, "prof1", "Content", 4, "Materia")

        Truth.assertThat(result.isFailure).isTrue()
        Truth.assertThat(result.exceptionOrNull()?.message).isEqualTo("User not found")
    }

    @Test
    fun `deleteReview llama al datasource correctamente`() = runTest {
        val reviewId = "rev_to_delete"
        coEvery { reviewDataSource.deleteReview(reviewId) } returns Unit

        val result = repository.deleteReview(reviewId)

        Truth.assertThat(result.isSuccess).isTrue()
        coVerify { reviewDataSource.deleteReview(reviewId) }
    }
}