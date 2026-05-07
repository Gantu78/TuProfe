package com.example.tuprofe.e2e

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import com.example.tuprofe.MainActivity
import com.example.tuprofe.data.datasource.AuthRemoteDataSource
import com.example.tuprofe.data.datasource.StorageRemoteDataSource
import com.example.tuprofe.data.datasource.impl.firestore.UserFirestoreDataSourceImpl
import com.example.tuprofe.data.repository.AuthRepository
import com.example.tuprofe.data.repository.UserRepository
import com.example.tuprofe.navegation.Screen
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
class ModifyUserE2E {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var composeRule = createAndroidComposeRule<MainActivity>()

    private lateinit var authRepository: AuthRepository

    private lateinit var userRepository: UserRepository

    @Before
    fun setup(){
        hiltRule.inject()

        try{
            Firebase.auth.useEmulator("10.0.2.2",9099)
            Firebase.firestore.useEmulator("10.0.2.2",8080)
            Firebase.storage.useEmulator("10.0.2.2",9199)
        } catch (e: Exception){

        }

        val authRemoteDataSource = AuthRemoteDataSource(Firebase.auth)
        val userRemoteDataSource = UserFirestoreDataSourceImpl(Firebase.firestore)
        val storageRemoteDataSource = StorageRemoteDataSource(Firebase.storage)

        authRepository = AuthRepository(authRemoteDataSource)
        userRepository = UserRepository(userRemoteDataSource,storageRemoteDataSource)

        runBlocking {
            authRepository.signUp("test@test.com","123456")
            authRepository.signIn("test@test.com","123456")

            val userId = authRepository.currentUser?.uid ?: return@runBlocking

            userRepository.registerUser(
                username = "testAndroid",
                carrera = "Test de android",
                userId = userId
            )
        }
    }


    @Test
    fun updateUserInfo_InfoUpdated(){
        composeRule.onNodeWithTag("mainScreen").assertIsDisplayed()

        composeRule.onNodeWithContentDescription(Screen.Configuracion.route).performClick()
        composeRule.onNodeWithTag("profileScreen").assertIsDisplayed()
        composeRule.onNodeWithTag("profileCard").performClick()
        composeRule.onNodeWithTag("configPerfilScreen").assertIsDisplayed()
        composeRule.onNodeWithTag("txtFieldUsername").assertTextContains("testAndroid", substring = true)

        composeRule.onNodeWithTag("txtFieldUsername").performTextClearance()
        composeRule.onNodeWithTag("txtFieldUsername").performTextInput("Modificado")
        composeRule.onNodeWithTag("btnGuardarCambios").performClick()
        composeRule.onNodeWithTag("saveConfirmationDialog").assertIsDisplayed()
        composeRule.onNodeWithTag("guardarCambiosbtn").performClick()


        composeRule.onNodeWithTag("profileScreen").assertIsDisplayed()

        composeRule.onNodeWithContentDescription(Screen.Main.route).performClick()
        composeRule.onNodeWithContentDescription(Screen.Configuracion.route).performClick()

        composeRule.onNodeWithTag("nombreEnTarjeta").assertTextEquals("Modificado")
    }

    @After
    fun tearDown()= runTest{
        val user = Firebase.auth.currentUser
        if(user != null){
            Firebase.auth.signOut()
        }
        user?.delete()?.await()
        Firebase.firestore.collection("users").document().delete().await()
    }
}