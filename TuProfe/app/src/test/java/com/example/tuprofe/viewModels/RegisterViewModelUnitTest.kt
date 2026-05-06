package com.example.tuprofe.viewModels

import android.util.Log
import com.example.tuprofe.data.repository.AuthRepository
import com.example.tuprofe.data.repository.UserRepository
import com.example.tuprofe.ui.register.RegisterViewModel
import com.google.common.truth.Truth.assertThat
import com.google.firebase.auth.FirebaseUser
import io.mockk.*
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
class RegisterViewModelUnitTest {

    private lateinit var viewModel: RegisterViewModel
    private lateinit var authRepository: AuthRepository
    private lateinit var userRepository: UserRepository

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0

        authRepository = mockk()
        userRepository = mockk()

        viewModel = RegisterViewModel(authRepository, userRepository, testDispatcher)
    }

    @After
    fun tearDown() {
        unmockkStatic(Log::class)
        Dispatchers.resetMain()
    }

    @Test
    fun `register_success_navigatesToHome`() = runTest {
        // Arrange
        val userId = "user123"
        val mockUser = mockk<FirebaseUser>()
        every { mockUser.uid } returns userId
        every { authRepository.currentUser } returns mockUser

        coEvery { authRepository.signUp(any(), any()) } returns Result.success(Unit)
        coEvery { userRepository.registerUser(any(), any(), userId) } returns Result.success(Unit)

        viewModel.setEmail("test@test.com")
        viewModel.setUsuario("carlitos")
        viewModel.setCarrera("Ingeniería")
        viewModel.setPassword1("123456")
        viewModel.setPassword2("123456")

        // Act
        viewModel.onRegisterClickSecure()

        // Assert
        val state = viewModel.uiState.value
        assertThat(state.navigateHome).isTrue()
        assertThat(state.mostrarMensaje).isTrue()
        assertThat(state.mostrarMensajeError).isFalse()
    }

    @Test
    fun `register_passwordsMismatch_showsError`() = runTest {
        // Arrange
        viewModel.setEmail("test@test.com")
        viewModel.setUsuario("user")
        viewModel.setCarrera("carrera")
        viewModel.setPassword1("123456")
        viewModel.setPassword2("654321")

        // Act
        viewModel.onRegisterClickSecure()

        // Assert
        val state = viewModel.uiState.value
        assertThat(state.mostrarMensajeError).isTrue()
        assertThat(state.errorMessage).isEqualTo("Las contraseñas no coinciden")
        assertThat(state.navigateHome).isFalse()
    }

    @Test
    fun `register_blankFields_showsError`() = runTest {
        // Arrange
        viewModel.setEmail("")

        // Act
        viewModel.onRegisterClickSecure()

        // Assert
        val state = viewModel.uiState.value
        assertThat(state.mostrarMensajeError).isTrue()
        assertThat(state.errorMessage).isEqualTo("Por favor complete todos los campos")
    }

    @Test
    fun `register_authFailure_showsErrorMessage`() = runTest {
        // Arrange
        val errorMsg = "Email inválido"
        coEvery { authRepository.signUp(any(), any()) } returns Result.failure(Exception(errorMsg))

        viewModel.setEmail("wrong@email")
        viewModel.setUsuario("user")
        viewModel.setCarrera("carrera")
        viewModel.setPassword1("123456")
        viewModel.setPassword2("123456")

        // Act
        viewModel.onRegisterClickSecure()

        // Assert
        val state = viewModel.uiState.value
        assertThat(state.mostrarMensajeError).isTrue()
        assertThat(state.errorMessage).isEqualTo(errorMsg)
        assertThat(state.navigateHome).isFalse()
    }

    @Test
    fun `register_firestoreFailure_showsErrorMessage`() = runTest {
        // Arrange
        val userId = "user123"
        val mockUser = mockk<FirebaseUser>()
        every { mockUser.uid } returns userId
        every { authRepository.currentUser } returns mockUser

        coEvery { authRepository.signUp(any(), any()) } returns Result.success(Unit)
        coEvery { userRepository.registerUser(any(), any(), userId) } returns
                Result.failure(Exception("Error en base de datos"))

        viewModel.setEmail("test@test.com")
        viewModel.setUsuario("user")
        viewModel.setCarrera("carrera")
        viewModel.setPassword1("123456")
        viewModel.setPassword2("123456")

        // Act
        viewModel.onRegisterClickSecure()

        // Assert
        val state = viewModel.uiState.value
        assertThat(state.mostrarMensajeError).isTrue()
        assertThat(state.errorMessage).isEqualTo("Error en base de datos")
        assertThat(state.navigateHome).isFalse()
    }
}