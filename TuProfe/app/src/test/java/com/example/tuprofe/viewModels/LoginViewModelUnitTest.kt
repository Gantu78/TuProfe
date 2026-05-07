package com.example.tuprofe.viewModels

import com.example.tuprofe.data.repository.AuthRepository
import com.example.tuprofe.ui.login.LoginViewModel
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class LoginViewModelUnitTest {

    private lateinit var viewModel: LoginViewModel
    private lateinit var authRepository: AuthRepository
    private val testDispatcher = StandardTestDispatcher()


    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        authRepository = mockk()
        viewModel = LoginViewModel(authRepository)
    }


    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }


    @Test
    fun login_success_updatesStateToNavigate() = runTest {
        // arrange
        val email = "test@example.com"
        val password = "password123"
        viewModel.setEmail(email)
        viewModel.setPassword(password)
        coEvery { authRepository.signIn(email, password) } returns Result.success(Unit)

        viewModel.loginClick()
        advanceUntilIdle()

        // assert
        val state = viewModel.uiState.value
        assertThat(state.navigate).isTrue()
        assertThat(state.mostrarMensajeError).isFalse()
    }


    @Test
    fun login_emptyFields_showsErrorMessage() = runTest {
        // arrange
        viewModel.setEmail("")
        viewModel.setPassword("")

        viewModel.loginClick()
        advanceUntilIdle()

        // assert
        val state = viewModel.uiState.value
        assertThat(state.mostrarMensajeError).isTrue()
        assertThat(state.errorMessage).isEqualTo("Por favor complete todos los campos")
        assertThat(state.navigate).isFalse()
    }


    @Test
    fun login_failure_showsErrorMessage() = runTest {
        // arrange
        val email = "test@example.com"
        val password = "wrong_password"
        val errorMsg = "Credenciales incorrectas"
        viewModel.setEmail(email)
        viewModel.setPassword(password)
        coEvery { authRepository.signIn(email, password) } returns Result.failure(Exception(errorMsg))

        viewModel.loginClick()
        advanceUntilIdle()

        // assert
        val state = viewModel.uiState.value
        assertThat(state.mostrarMensajeError).isTrue()
        assertThat(state.errorMessage).isEqualTo(errorMsg)
        assertThat(state.navigate).isFalse()
    }

    @Test
    fun setEmail_updatesStateCorrectly() {
        // arrange
        val email = "new@example.com"

        viewModel.setEmail(email)

        // assert
        assertThat(viewModel.uiState.value.email).isEqualTo(email)
    }

    @Test
    fun setPassword_updatesStateCorrectly() {
        // arrange
        val password = "new_password"

        viewModel.setPassword(password)

        // assert
        assertThat(viewModel.uiState.value.password).isEqualTo(password)
    }

    @Test
    fun togglePasswordVisibility_changesState() {
        // arrange
        val initialState = viewModel.uiState.value.passwordVisible

        viewModel.togglePasswordVisibility()

        // assert
        assertThat(viewModel.uiState.value.passwordVisible).isNotEqualTo(initialState)
    }
}