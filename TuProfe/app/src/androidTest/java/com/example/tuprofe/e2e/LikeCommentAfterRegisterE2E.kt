package com.example.tuprofe.e2e

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.test.espresso.Espresso
import com.example.tuprofe.MainActivity
import com.example.tuprofe.data.datasource.AuthRemoteDataSource
import com.example.tuprofe.data.datasource.StorageRemoteDataSource
import com.example.tuprofe.data.datasource.impl.firestore.UserFirestoreDataSourceImpl
import com.example.tuprofe.data.repository.AuthRepository
import com.example.tuprofe.data.repository.UserRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class LikeCommentAfterRegisterE2E {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var composeRule = createAndroidComposeRule<MainActivity>()

    private lateinit var authRepository: AuthRepository
    private lateinit var userRepository: UserRepository

    private var seedUserId = ""
    private var seedReviewId = ""
    private var seedCommentId = ""

    @Before
    fun setup() {
        hiltRule.inject()

        try {
            Firebase.auth.useEmulator("10.0.2.2", 9099)
            Firebase.firestore.useEmulator("10.0.2.2", 8080)
            Firebase.storage.useEmulator("10.0.2.2", 9199)
        } catch (e: Exception) { }

        val authRemoteDataSource = AuthRemoteDataSource(Firebase.auth)
        val userRemoteDataSource = UserFirestoreDataSourceImpl(Firebase.firestore)
        val storageRemoteDataSource = StorageRemoteDataSource(Firebase.storage)

        authRepository = AuthRepository(authRemoteDataSource)
        userRepository = UserRepository(userRemoteDataSource, storageRemoteDataSource)

        runBlocking {
            // Crear usuario semilla con reseña y comentario en el emulador
            authRepository.signUp("seed@seed.com", "123456")
            seedUserId = authRepository.currentUser?.uid ?: ""
            userRepository.registerUser("seedUser", "Ingeniería", seedUserId)

            val reviewRef = Firebase.firestore.collection("reviews").add(
                mapOf(
                    "userId" to seedUserId,
                    "professorId" to "prof_seed",
                    "content" to "Excelente profesor, explica muy bien los temas",
                    "time" to "2026-01-01T00:00:00.000Z",
                    "rating" to 5,
                    "materia" to "Programación",
                    "professor" to mapOf("name" to "Prof. Semilla", "foto" to null),
                    "user" to mapOf("id" to seedUserId, "username" to "seedUser"),
                    "likesCount" to 0,
                    "comment" to 1
                )
            ).await()
            seedReviewId = reviewRef.id

            val commentRef = Firebase.firestore.collection("comments").add(
                mapOf(
                    "reviewId" to seedReviewId,
                    "parentCommentId" to null,
                    "userId" to seedUserId,
                    "content" to "Totalmente de acuerdo con esta reseña",
                    "createdAt" to "2026-01-01T00:00:00.000Z",
                    "likesCount" to 0,
                    "repliesCount" to 0,
                    "user" to mapOf("id" to seedUserId, "username" to "seedUser", "foto" to null)
                )
            ).await()
            seedCommentId = commentRef.id

            // Cerrar sesión para que la app arranque en LoginScreen
            authRepository.signOut()
        }
    }

    @Test
    fun registerWithShortPassword_thenFix_likeAndUnlikeComment() {
        // Ir a la pantalla de registro
        composeRule.onNodeWithTag("register_button").performClick()

        // Llenar formulario con contraseña corta (< 6 caracteres)
        composeRule.onNodeWithTag("txtEmail").performTextInput("newuser@test.com")
        composeRule.onNodeWithTag("txtUsuario").performTextInput("newUser")
        composeRule.onNodeWithTag("txtCarrera").performTextInput("Sistemas")
        composeRule.onNodeWithTag("txtContraseña").performTextInput("1234")
        composeRule.onNodeWithTag("txtContraseña2").performTextInput("1234")
        composeRule.onNodeWithTag("registerbutton").performClick()

        // Verificar que se muestra el mensaje de error
        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("error_message").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithTag("error_message").assertIsDisplayed()

        // Corregir contraseña a una válida
        composeRule.onNodeWithTag("txtContraseña").performTextClearance()
        composeRule.onNodeWithTag("txtContraseña2").performTextClearance()
        composeRule.onNodeWithTag("txtContraseña").performTextInput("123456")
        composeRule.onNodeWithTag("txtContraseña2").performTextInput("123456")
        composeRule.onNodeWithTag("registerbutton").performClick()

        // Esperar navegación a la pantalla principal
        composeRule.waitUntil(timeoutMillis = 8000) {
            composeRule.onAllNodesWithTag("mainScreen").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithTag("mainScreen").assertIsDisplayed()

        // Esperar a que carguen las reseñas y entrar a la primera
        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("review_card").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onAllNodesWithTag("review_card").onFirst().performClick()

        // Verificar que el detalle de la reseña se muestra correctamente
        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("detalleScreen").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithTag("detalleScreen").assertIsDisplayed()

        // Esperar a que carguen los comentarios y entrar al primero
        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("comment_card").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onAllNodesWithTag("comment_card").onFirst().performClick()

        // Verificar la pantalla de detalle del comentario
        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("commentDetalleScreen").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithTag("commentDetalleScreen").assertIsDisplayed()

        // El comentario empieza con 0 likes (dato de semilla)
        composeRule.onNodeWithTag("comment_likes_count").assertTextEquals("0 Likes")

        // Dar like al comentario
        composeRule.onNodeWithContentDescription("Like").performClick()

        // Verificar que los likes aumentaron a 1
        composeRule.waitUntil(timeoutMillis = 5000) {
            val text = composeRule.onAllNodesWithTag("comment_likes_count")
                .fetchSemanticsNodes().firstOrNull()
                ?.config?.get(SemanticsProperties.Text)?.firstOrNull()?.text ?: ""
            text == "1 Likes"
        }
        composeRule.onNodeWithTag("comment_likes_count").assertTextEquals("1 Likes")

        // Volver al detalle de la reseña y al home
        Espresso.pressBack()
        composeRule.waitUntil(timeoutMillis = 3000) {
            composeRule.onAllNodesWithTag("detalleScreen").fetchSemanticsNodes().isNotEmpty()
        }
        Espresso.pressBack()
        composeRule.waitUntil(timeoutMillis = 3000) {
            composeRule.onAllNodesWithTag("mainScreen").fetchSemanticsNodes().isNotEmpty()
        }

        // Volver a entrar a la misma reseña y al mismo comentario
        composeRule.onAllNodesWithTag("review_card").onFirst().performClick()
        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("comment_card").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onAllNodesWithTag("comment_card").onFirst().performClick()
        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("commentDetalleScreen").fetchSemanticsNodes().isNotEmpty()
        }

        // Quitar el like
        composeRule.onNodeWithContentDescription("Like").performClick()

        // Verificar que los likes volvieron a 0
        composeRule.waitUntil(timeoutMillis = 5000) {
            val text = composeRule.onAllNodesWithTag("comment_likes_count")
                .fetchSemanticsNodes().firstOrNull()
                ?.config?.get(SemanticsProperties.Text)?.firstOrNull()?.text ?: ""
            text == "0 Likes"
        }
        composeRule.onNodeWithTag("comment_likes_count").assertTextEquals("0 Likes")
    }

    @After
    fun tearDown() = runTest {
        Firebase.auth.signOut()
        if (seedCommentId.isNotEmpty())
            Firebase.firestore.collection("comments").document(seedCommentId).delete().await()
        if (seedReviewId.isNotEmpty())
            Firebase.firestore.collection("reviews").document(seedReviewId).delete().await()
        if (seedUserId.isNotEmpty())
            Firebase.firestore.collection("users").document(seedUserId).delete().await()
        Firebase.auth.currentUser?.delete()?.await()
    }
}
