package com.example.tuprofe.e2e

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
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
class FollowUserE2E {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var composeRule = createAndroidComposeRule<MainActivity>()

    private lateinit var authRepository: AuthRepository
    private lateinit var userRepository: UserRepository

    private var userAId = ""
    private var userBId = ""
    private var seedReviewId = ""

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
            // Crear usuario B con una reseña (será el usuario a seguir)
            authRepository.signUp("seed_userb@seed.com", "123456")
            userBId = authRepository.currentUser?.uid ?: ""
            userRepository.registerUser("userBAndroid", "Test de android", userBId)

            val reviewRef = Firebase.firestore.collection("reviews").add(
                mapOf(
                    "userId" to userBId,
                    "professorId" to "prof_seed",
                    "content" to "Buena clase de programación",
                    "time" to "2026-01-01T00:00:00.000Z",
                    "rating" to 4,
                    "materia" to "Programación",
                    "professor" to mapOf("name" to "Prof. Prueba", "foto" to null),
                    "user" to mapOf("id" to userBId, "username" to "userBAndroid"),
                    "likesCount" to 0,
                    "comment" to 0
                )
            ).await()
            seedReviewId = reviewRef.id

            // Crear usuario A y dejar la sesión activa (la app arranca en mainScreen)
            authRepository.signOut()
            authRepository.signUp("usera_test@test.com", "123456")
            userAId = authRepository.currentUser?.uid ?: ""
            userRepository.registerUser("userAAndroid", "Sistemas", userAId)
        }
    }

    @Test
    fun followUser_fromProfile_reviewAppearsInSiguiendoTab() {
        // La app inicia en mainScreen porque userA tiene sesión activa
        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("mainScreen").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithTag("mainScreen").assertIsDisplayed()

        // Esperar a que cargue la reseña de userB y navegar a su perfil
        composeRule.waitUntil(timeoutMillis = 8000) {
            composeRule.onAllNodesWithText("Por: @userBAndroid", substring = true)
                .fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onAllNodesWithText("Por: @userBAndroid", substring = true)
            .onFirst().performClick()

        // Verificar que se abre el perfil de userB
        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("userProfileScreen").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithTag("userProfileScreen").assertIsDisplayed()

        // Esperar a que cargue la información del perfil
        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("user_profile_nombre").fetchSemanticsNodes().isNotEmpty()
        }

        // Verificar la información del perfil
        composeRule.onNodeWithTag("user_profile_nombre").assertTextEquals("userBAndroid")
        composeRule.onNodeWithTag("user_profile_carrera").assertTextEquals("Test de android")
        composeRule.onNodeWithTag("user_profile_followers_count", useUnmergedTree = true).assertTextEquals("0")

        // Dar follow
        composeRule.onNodeWithTag("follow_button").performClick()

        // Verificar que el contador de seguidores aumentó a 1
        composeRule.waitUntil(timeoutMillis = 5000) {
            val text = composeRule.onAllNodesWithTag("user_profile_followers_count", useUnmergedTree = true)
                .fetchSemanticsNodes().firstOrNull()
                ?.config?.get(SemanticsProperties.Text)?.firstOrNull()?.text ?: ""
            text == "1"
        }
        composeRule.onNodeWithTag("user_profile_followers_count", useUnmergedTree = true).assertTextEquals("1")

        // Volver al mainScreen
        Espresso.pressBack()
        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithTag("mainScreen").fetchSemanticsNodes().isNotEmpty()
        }

        // Ir a la pestaña Siguiendo
        composeRule.onNodeWithTag("tab_siguiendo").performClick()

        // Verificar que aparece la reseña de userB en la pestaña Siguiendo
        composeRule.waitUntil(timeoutMillis = 8000) {
            composeRule.onAllNodesWithText("Por: @userBAndroid", substring = true)
                .fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onAllNodesWithText("Por: @userBAndroid", substring = true)
            .onFirst().assertIsDisplayed()
    }

    @After
    fun tearDown() = runTest {
        if (seedReviewId.isNotEmpty())
            Firebase.firestore.collection("reviews").document(seedReviewId).delete().await()
        if (userBId.isNotEmpty())
            Firebase.firestore.collection("users").document(userBId).delete().await()
        if (userAId.isNotEmpty())
            Firebase.firestore.collection("users").document(userAId).delete().await()

        // Eliminar cuenta de userA (actualmente en sesión al final del test)
        Firebase.auth.currentUser?.delete()?.await()
        Firebase.auth.signOut()

        // Iniciar sesión como userB para eliminar su cuenta de auth
        try {
            Firebase.auth.signInWithEmailAndPassword("seed_userb@seed.com", "123456").await()
            Firebase.auth.currentUser?.delete()?.await()
            Firebase.auth.signOut()
        } catch (e: Exception) { }
    }
}
