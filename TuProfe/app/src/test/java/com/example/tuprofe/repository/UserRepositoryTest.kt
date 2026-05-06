package com.example.tuprofe.repository

import com.example.tuprofe.data.Usuario
import com.example.tuprofe.data.datasource.StorageRemoteDataSource
import com.example.tuprofe.data.datasource.UserRemoteDataSource
import com.example.tuprofe.data.dtos.UserDto
import com.example.tuprofe.data.repository.UserRepository
import com.google.common.truth.Truth
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class UserRepositoryTest {

    private val mockDataSource = mockk<UserRemoteDataSource>()
    private val storageDataSource = mockk<StorageRemoteDataSource>()

    private val repository = UserRepository(mockDataSource, storageDataSource)

    @Test
    fun `al llamar getUserById, si el id es valido retorna un Result con el userInfo`() = runTest {
        // Arrange
        val userId = "123"
        val userDto = UserDto(
            id = "123",
            username = "Carlitos",
            email = "carlitos@test.com",
            carrera = "Ingeniería",
            foto = "url_foto"
        )

        coEvery { mockDataSource.getUserById(userId, "") } returns userDto

        // Act
        val result = repository.getUserById(userId)

        // Assert
        Truth.assertThat(result.isSuccess).isTrue()
        val user = result.getOrNull()
        Truth.assertThat(user?.usuarioId).isEqualTo(userId)
        Truth.assertThat(user?.nombreUsu).isEqualTo("Carlitos")
    }

    @Test
    fun `al llamar getUserById, si el id es invalido retorna un Result con failure`() = runTest {
        // Arrange
        val userId = "666"
        coEvery { mockDataSource.getUserById(userId, "") } returns null

        // Act
        val result = repository.getUserById(userId)

        // Assert
        Truth.assertThat(result.isFailure).isTrue()
        Truth.assertThat(result.exceptionOrNull()?.message).isEqualTo("User not found")
    }

    @Test
    fun `al llamar getUserById, si el datasource devuelve campos vacios se mapean correctamente`() = runTest {
        // Arrange
        val userId = "123"
        val userDto = UserDto(
            id = userId,
            username = null,
            email = null,
            carrera = null,
            foto = null
        )

        coEvery { mockDataSource.getUserById(userId, "") } returns userDto
        coEvery { storageDataSource.getProfileImageUrl(userId) } returns "default_url"

        // Act
        val result = repository.getUserById(userId)

        // Assert
        Truth.assertThat(result.isSuccess).isTrue()
        val user = result.getOrNull()
        Truth.assertThat(user?.nombreUsu).isEqualTo("Usuario $userId")
        Truth.assertThat(user?.email).isEqualTo("Email@papu.com")
        Truth.assertThat(user?.carrera).isEqualTo("Ingenieria de la vida")
        Truth.assertThat(user?.imageprofeUrl).isEqualTo("default_url")
    }

    @Test
    fun `updateUser llama al datasource correctamente`() = runTest {
        // Arrange
        val userId = "123"
        val username = "NuevoNombre"
        val email = "nuevo@email.com"
        val carrera = "NuevaCarrera"
        coEvery { mockDataSource.updateUser(userId, username, email, carrera) } returns Unit

        // Act
        val result = repository.updateUser(userId, username, email, carrera)

        // Assert
        Truth.assertThat(result.isSuccess).isTrue()
        coVerify { mockDataSource.updateUser(userId, username, email, carrera) }
    }

    @Test
    fun `followOrUnfollow llama al datasource correctamente`() = runTest {
        // Arrange
        val currentUserId = "papu yo"
        val targetUserId = "papu tu"
        coEvery { mockDataSource.followOrUnfollowUser(currentUserId, targetUserId) } returns Unit

        // Act
        val result = repository.followOrUnfollow(currentUserId, targetUserId)

        // Assert
        Truth.assertThat(result.isSuccess).isTrue()
        coVerify { mockDataSource.followOrUnfollowUser(currentUserId, targetUserId) }
    }
}
