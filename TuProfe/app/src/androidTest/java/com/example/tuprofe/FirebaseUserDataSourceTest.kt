package com.example.tuprofe

import com.example.tuprofe.data.datasource.impl.firestore.UserFirestoreDataSourceImpl
import com.example.tuprofe.data.dtos.UserDto
import com.example.tuprofe.data.dtos.RegisterUserDto
import com.google.common.truth.Truth
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

class FirebaseUserDataSourceTest {


    private val db = Firebase.firestore

    private lateinit var dataSource: UserFirestoreDataSourceImpl

    // Genera un UserDto con datos deterministas a partir de un índice.
    private fun generateUser(i: Int): UserDto = UserDto(
        id = "test_user_$i",
        name = "Test Name $i",
        username = "testuser$i",
        email = "testuser$i@test.com",
        carrera = "Ingeniería de Sistemas",
        foto = null,
        followingCount = 0,
        followersCount = 0,
        followed = false
    )

    @Before
    fun setup() = runTest{
        try {
            db.useEmulator("10.0.2.2", 8080)
        }catch (e: Exception){

        }
        dataSource = UserFirestoreDataSourceImpl(db)

        val batch = db.batch()
        repeat(10) { i ->
            val user = generateUser(i)
            batch.set(db.collection("users").document(user.id), user)
        }

        batch.commit().await()
    }

    @Test
    fun getUserById_validId_correctUser() = runTest {

        //Arrange

        val id = "test_user_9"
        val expectedName = "Test Name 9"

        //Act

        val result = dataSource.getUserById(id)

        //Assert

        Truth.assertThat(result).isNotNull()
        Truth.assertThat(result?.name).isEqualTo(expectedName)
        Truth.assertThat(result?.id).isEqualTo(id)

    }

    @Test
    fun getUserById_invalidId_correctUser() = runTest {
        //Arrange

        val id = "test_user_99"
        val expectedName = "Test Name 99"

        //Act

        val result = dataSource.getUserById(id)

        //Assert

        Truth.assertThat(result).isNull()

    }


    @Test
    fun registerUser_insertDocument_DocumentExists() = runTest {

        //Arrange
        val user = RegisterUserDto(
            username = "testuser10",
            carrera = "Ingeniería de Sistemas",
            FCMToken = "test_token"
        )

        //Act
        dataSource.registerUser(user, "999")

        //Assert

        val result = dataSource.getUserById("999")

        Truth.assertThat(result).isNotNull()
        Truth.assertThat(result?.id).isEqualTo("999")
        Truth.assertThat(result?.username).isEqualTo(user.username)
        Truth.assertThat(result?.carrera).isEqualTo(user.carrera)
    }

    @After
    fun teardown() = runTest {
        val users = db.collection("users").get().await()
        users.documents.forEach { doc ->
            db.collection("users").document(doc.id).delete().await()
        }
    }
}
