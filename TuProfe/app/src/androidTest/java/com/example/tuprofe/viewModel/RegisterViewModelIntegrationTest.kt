package com.example.tuprofe.viewModelimport

import com.example.tuprofe.data.datasource.AuthRemoteDataSource

import com.example.tuprofe.data.datasource.StorageRemoteDataSource
import com.example.tuprofe.data.datasource.impl.firestore.UserFirestoreDataSourceImpl
import com.example.tuprofe.data.repository.AuthRepository
import com.example.tuprofe.data.repository.UserRepository
import com.example.tuprofe.ui.register.RegisterViewModel
import com.google.common.truth.Truth.assertThat
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class RegisterViewModelIntegrationTest {

    private lateinit var viewModel: RegisterViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp(){

        Dispatchers.setMain(testDispatcher)

        try {
            Firebase.auth.useEmulator("10.0.2.2", 9099)
            Firebase.firestore.useEmulator("10.0.2.2", 8080)
        } catch (e: Exception) {

        }


        Firebase.auth.signOut()

        val authRepository = AuthRepository(AuthRemoteDataSource(Firebase.auth))
        val userRepository = UserRepository(
            UserFirestoreDataSourceImpl(Firebase.firestore),
            StorageRemoteDataSource(Firebase.storage)
        )


        viewModel = RegisterViewModel(authRepository, userRepository, testDispatcher)
    }

    @Test
    fun register_success_createUserAndUpdateUI() = runBlocking {

        val testEmail = "test_${System.currentTimeMillis()}@papu.com"
        viewModel.setEmail(testEmail)
        viewModel.setUsuario("TestUser")
        viewModel.setCarrera("Ingeniería")
        viewModel.setPassword1("password123")
        viewModel.setPassword2("password123")


        viewModel.onRegisterClickSecure()


        val timeout = 15000L
        val start = System.currentTimeMillis()

        var state = viewModel.uiState.value
        while (!state.navigateHome && !state.mostrarMensajeError && (System.currentTimeMillis() - start) < timeout) {
            delay(500)
            state = viewModel.uiState.value
        }

        // Si el test falla, este log te dirá por qué falló Firebase
        if (state.mostrarMensajeError) {
            println("FIREBASE ERROR DETECTADO: ${state.errorMessage}")
        }


        assertThat(state.errorMessage).isEmpty()
        assertThat(state.mostrarMensajeError).isFalse()
        assertThat(state.navigateHome).isTrue()
        assertThat(state.mostrarMensaje).isTrue()
    }

    @Test
    fun register_alreadyUsedEmail_showErrorMessage() = runBlocking {

        val duplicatedEmail = "alreadyused_${System.currentTimeMillis()}@papu.com"

        val authRepository = AuthRepository(AuthRemoteDataSource(Firebase.auth))
        authRepository.signUp(duplicatedEmail, "password123")
        Firebase.auth.signOut()

        viewModel.setEmail(duplicatedEmail)
        viewModel.setUsuario("TestUser")
        viewModel.setCarrera("Ingeniería")
        viewModel.setPassword1("password123")
        viewModel.setPassword2("password123")

        viewModel.onRegisterClickSecure()

        val timeout = 10000L
        val start = System.currentTimeMillis()
        var state = viewModel.uiState.value
        while (!state.mostrarMensajeError && (System.currentTimeMillis() - start) < timeout) {
            delay(500)
            state = viewModel.uiState.value
        }

        assertThat(state.mostrarMensajeError).isTrue()
        assertThat(state.errorMessage).isEqualTo("El usuario ya está registrado")
        assertThat(state.navigateHome).isFalse()
    }


    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}