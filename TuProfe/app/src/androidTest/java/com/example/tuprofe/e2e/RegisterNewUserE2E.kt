package com.example.tuprofe.e2e

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
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
class RegisterNewUserE2E {
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
            authRepository.signUp("admin@admin.com","123456")
            authRepository.signOut()
        }
    }

    @After
    fun tearDown()= runTest {
        val user = Firebase.auth.currentUser
        if(user != null){
            Firebase.auth.signOut()
        }
        user?.delete()?.await()
    }

    @Test
    fun navigate_fromStart_toLogin(){
        composeRule.onNodeWithTag("login_button").performClick()

        composeRule.onNodeWithTag("loginScreen").assertIsDisplayed()
    }

    @Test
    fun registerUser_shortPassword_showMessage(){
        composeRule.onNodeWithTag("register_button").performClick()

        composeRule.onNodeWithTag("txtEmail").performTextInput("emailT@gmail.com")
        composeRule.onNodeWithTag("txtUsuario").performTextInput("AndroidTest")
        composeRule.onNodeWithTag("txtCarrera").performTextInput("Test de android")
        composeRule.onNodeWithTag("txtContraseña").performTextInput("1234")
        composeRule.onNodeWithTag("txtContraseña2").performTextInput("1234")

        composeRule.onNodeWithTag("registerbutton").performClick()

        composeRule.onNodeWithTag("error_message").assertIsDisplayed()
    }

    @Test
    fun registerUser_repeatedEmail_showMessage(){
        composeRule.onNodeWithTag("register_button").performClick()

        composeRule.onNodeWithTag("txtEmail").performTextInput("admin@admin.com")
        composeRule.onNodeWithTag("txtUsuario").performTextInput("AndroidTest")
        composeRule.onNodeWithTag("txtCarrera").performTextInput("Test de android")
        composeRule.onNodeWithTag("txtContraseña").performTextInput("123456")
        composeRule.onNodeWithTag("txtContraseña2").performTextInput("123456")

        composeRule.onNodeWithTag("registerbutton").performClick()

        composeRule.onNodeWithTag("error_message").assertIsDisplayed()
    }

    @Test
    fun registerUser_allValidInputs_navigateHome(){
        composeRule.onNodeWithTag("register_button").performClick()

        composeRule.onNodeWithTag("txtEmail").performTextInput("test1@android.com")
        composeRule.onNodeWithTag("txtUsuario").performTextInput("AndroidTest")
        composeRule.onNodeWithTag("txtCarrera").performTextInput("Test de android")
        composeRule.onNodeWithTag("txtContraseña").performTextInput("123456")
        composeRule.onNodeWithTag("txtContraseña2").performTextInput("123456")

        composeRule.onNodeWithTag("registerbutton").performClick()

        composeRule.waitUntil(timeoutMillis = 5000){
            composeRule.onAllNodesWithTag("mainScreen").fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onNodeWithTag("mainScreen").assertIsDisplayed()
    }


}