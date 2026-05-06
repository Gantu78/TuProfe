package com.example.tuprofe

import com.example.tuprofe.data.datasource.impl.firestore.ReviewFirestoreDataSourceImpl
import com.example.tuprofe.data.dtos.CreateReviewDto
import com.example.tuprofe.data.dtos.CreateReviewProfessorDto
import com.example.tuprofe.data.dtos.CreateReviewUserDto
import com.example.tuprofe.data.dtos.ProfessorDto
import com.example.tuprofe.data.dtos.UserDto
import com.google.common.truth.Truth
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class ReviewFirebaseDataSourceTest {

    private val db = Firebase.firestore
    private lateinit var reviewDataSource: ReviewFirestoreDataSourceImpl

    private fun generateUser(i: Int): UserDto = UserDto(
        id = "test_user_$i",
        name = "Test Name $i",
        username = "testuser$i",
        email = "testuser$i@test.com",
        carrera = "Ingeniería de Sistemas",
        foto = null,
        followingCount = 0,
        followersCount = 0,
        followed = false
    )

    private fun generateProfessor(i: Int): ProfessorDto = ProfessorDto(
        id = "test_professor_$i",
        name = "Test Name $i",
        department = "Test Department $i",
        subjects = listOf("Test Subject $i"),
        foto_prof = null,
        createdAt = "2023-05-01T12:00:00.000Z",
        updatedAt = "2023-05-01T12:00:00.000Z"
    )

    private fun generateCreateReviewDto(userIndex: Int, profIndex: Int = userIndex): CreateReviewDto {
        val user = generateUser(userIndex)
        val prof = generateProfessor(profIndex)
        return CreateReviewDto(
            userId = user.id,
            professorId = prof.id,
            content = "Test content $userIndex",
            rating = (userIndex % 5) + 1,
            time = "2023-05-01T12:00:00.000Z",
            materia = "Test Materia $userIndex",
            user = CreateReviewUserDto(username = user.username),
            professor = CreateReviewProfessorDto(name = prof.name, department = prof.department, foto = prof.foto_prof)
        )
    }

    // Inserta una review directamente en Firestore y devuelve su ID auto-generado.
    private suspend fun insertReview(userIndex: Int, profIndex: Int = userIndex): String {
        val dto = generateCreateReviewDto(userIndex, profIndex)
        return db.collection("reviews").add(dto).await().id
    }

    @Before
    fun setup() = runTest {
        try {
            db.useEmulator("10.0.2.2", 8080)
        } catch (_: Exception) {}
        reviewDataSource = ReviewFirestoreDataSourceImpl(db)

        val batch = db.batch()
        repeat(10) { i ->
            val user = generateUser(i)
            batch.set(db.collection("users").document(user.id), user)
        }
        batch.commit().await()
    }

    @After
    fun teardown() = runTest {
        val reviews = db.collection("reviews").get().await()
        for (review in reviews) {
            review.reference.collection("likes").get().await()
                .forEach { it.reference.delete().await() }
            review.reference.delete().await()
        }
        db.collection("users").get().await()
            .documents.forEach { it.reference.delete().await() }
    }

    @Test
    fun getUserReviews_validId_correctReviews() = runTest {
        val dto = generateCreateReviewDto(1)
        reviewDataSource.createReview(dto)

        val reviews = reviewDataSource.getUserReviews(dto.userId!!)

        Truth.assertThat(reviews).isNotEmpty()
        Truth.assertThat(reviews.first().content).isEqualTo(dto.content)
    }

    @Test
    fun createReview_documentCreated_appearsInGetAll() = runTest {
        val dto = generateCreateReviewDto(1)

        reviewDataSource.createReview(dto)

        val all = reviewDataSource.getAllReviews()
        Truth.assertThat(all).isNotEmpty()
        Truth.assertThat(all.any { it.content == dto.content }).isTrue()
    }

    @Test
    fun getAllReviews_multipleCreated_returnsAll() = runTest {
        repeat(3) { i -> reviewDataSource.createReview(generateCreateReviewDto(i)) }

        val reviews = reviewDataSource.getAllReviews()

        Truth.assertThat(reviews.size).isAtLeast(3)
    }

    @Test
    fun getReviewById_validId_correctReview() = runTest {
        val dto = generateCreateReviewDto(1)
        val id = insertReview(1)

        val result = reviewDataSource.getReviewById(id, "")

        Truth.assertThat(result.content).isEqualTo(dto.content)
        Truth.assertThat(result.rating).isEqualTo(dto.rating)
    }

    @Test
    fun deleteReview_deletesDocument_notInGetAll() = runTest {
        val id = insertReview(1)

        reviewDataSource.deleteReview(id)

        val reviews = reviewDataSource.getAllReviews()
        Truth.assertThat(reviews.none { it.id == id }).isTrue()
    }

    @Test
    fun updateReview_changesContent_contentUpdated() = runTest {
        val id = insertReview(1)
        val update = CreateReviewDto(content = "Updated content", rating = 3)

        reviewDataSource.updateReview(id, update)

        val result = reviewDataSource.getReviewById(id, "")
        Truth.assertThat(result.content).isEqualTo("Updated content")
        Truth.assertThat(result.rating).isEqualTo(3)
    }
}