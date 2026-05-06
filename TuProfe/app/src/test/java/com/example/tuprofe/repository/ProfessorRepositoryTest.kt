package com.example.tuprofe.repository

import com.example.tuprofe.data.datasource.ProfessorRemoteDataSource
import com.example.tuprofe.data.dtos.ProfessorDto
import com.example.tuprofe.data.repository.ProfessorRepository
import com.google.common.truth.Truth
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ProfessorRepositoryTest {

    private val mockDataSource = mockk<ProfessorRemoteDataSource>()
    private val repository = ProfessorRepository(mockDataSource)

    @Test
    fun `getProfessors retorna lista de profesores mapeada correctamente`() = runTest {
        // Arrange
        val profDto = ProfessorDto(
            id = "1",
            name = "Juan Perez",
            department = "Matematicas",
            subjects = listOf("Algebra"),
            foto_prof = "url",
            createdAt = "",
            updatedAt = ""
        )
        coEvery { mockDataSource.getAllProfessors() } returns listOf(profDto)

        // Act
        val result = repository.getProfessors()

        // Assert
        Truth.assertThat(result.isSuccess).isTrue()
        val list = result.getOrNull()
        Truth.assertThat(list).hasSize(1)
        Truth.assertThat(list?.first()?.nombreProfe).isEqualTo("Juan Perez")
        Truth.assertThat(list?.first()?.profeId).isEqualTo("1")
    }

    @Test
    fun `getProfessors retorna failure cuando el datasource falla`() = runTest {
        // Arrange
        val exception = Exception("Network Error")
        coEvery { mockDataSource.getAllProfessors() } throws exception

        // Act
        val result = repository.getProfessors()

        // Assert
        Truth.assertThat(result.isFailure).isTrue()
        Truth.assertThat(result.exceptionOrNull()).isEqualTo(exception)
    }

    @Test
    fun `getProfessorById retorna profesor mapeado correctamente`() = runTest {
        // Arrange
        val profId = "2"
        val profDto = ProfessorDto(
            id = profId,
            name = "Maria Lopez",
            department = "Fisica",
            subjects = listOf("Mecanica"),
            foto_prof = "url2",
            createdAt = "",
            updatedAt = ""
        )
        coEvery { mockDataSource.getProfessorById(profId) } returns profDto

        // Act
        val result = repository.getProfessorById(profId)

        // Assert
        Truth.assertThat(result.isSuccess).isTrue()
        val prof = result.getOrNull()
        Truth.assertThat(prof?.nombreProfe).isEqualTo("Maria Lopez")
        Truth.assertThat(prof?.profeId).isEqualTo(profId)
    }

    @Test
    fun `getProfessorById retorna failure cuando el ID no existe o falla`() = runTest {
        // Arrange
        val profId = "999"
        coEvery { mockDataSource.getProfessorById(profId) } throws Exception("Not found")

        // Act
        val result = repository.getProfessorById(profId)

        // Assert
        Truth.assertThat(result.isFailure).isTrue()
        Truth.assertThat(result.exceptionOrNull()?.message).isEqualTo("Not found")
    }
}
