package com.example.tuprofe.viewModels

import com.example.tuprofe.data.repository.AuthRepository
import com.example.tuprofe.ui.login.LoginViewModel
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelUnitTest {

    private lateinit var viewModel: LoginViewModel
    private lateinit var authRepository: AuthRepository

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        authRepository = mockk()
        viewModel = LoginViewModel(authRepository)
    }


    @Test
    fun `login_success_updatesStateToNavigate`() = runTest {
        // Arrange
        val email = "test@example.com"
        val password = "password123"
        coEvery { authRepository.signIn(email, password) } returns Result.success(Unit)

        viewModel.setEmail(email)
        viewModel.setPassword(password)

        // Act
        viewModel.loginClick()

        // Assert
        val state = viewModel.uiState.value
        assertThat(state.navigate).isTrue()
        assertThat(state.mostrarMensajeError).isFalse()
    }

    @Test
    fun `login_emptyFields_showsErrorMessage`() = runTest {
        // Arrange
        viewModel.setEmail("")
        viewModel.setPassword("")

        // Act
        viewModel.loginClick()

        // Assert
        val state = viewModel.uiState.value
        assertThat(state.mostrarMensajeError).isTrue()
        assertThat(state.errorMessage).isEqualTo("Por favor complete todos los campos")
        assertThat(state.navigate).isFalse()
    }

    @Test
    fun `login_failure_showsErrorMessage`() = runTest {
        // Arrange
        val email = "test@example.com"
        val password = "wrong_password"
        val errorMsg = "Credenciales incorrectas"
        coEvery { authRepository.signIn(email, password) } returns Result.failure(Exception(errorMsg))

        viewModel.setEmail(email)
        viewModel.setPassword(password)

        // Act
        viewModel.loginClick()

        // Assert
        val state = viewModel.uiState.value
        assertThat(state.mostrarMensajeError).isTrue()
        assertThat(state.errorMessage).isEqualTo(errorMsg)
        assertThat(state.navigate).isFalse()
    }

    @Test
    fun `setEmail_updatesStateCorrectly`() = runTest {
        // Arrange
        val email = "new@example.com"

        // Act
        viewModel.setEmail(email)

        // Assert
        assertThat(viewModel.uiState.value.email).isEqualTo(email)
    }

    @Test
    fun `setPassword_updatesStateCorrectly`() = runTest {
        // Arrange
        val password = "new_password"

        // Act
        viewModel.setPassword(password)

        // Assert
        assertThat(viewModel.uiState.value.password).isEqualTo(password)
    }

    @Test
    fun `togglePasswordVisibility_changesState`() = runTest {
        // Arrange
        val initialState = viewModel.uiState.value.passwordVisible

        // Act
        viewModel.togglePasswordVisibility()

        // Assert
        assertThat(viewModel.uiState.value.passwordVisible).isNotEqualTo(initialState)
    }
}
