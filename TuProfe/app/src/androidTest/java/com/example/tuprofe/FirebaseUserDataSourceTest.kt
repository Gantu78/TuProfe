package com.example.tuprofe

import com.example.tuprofe.data.datasource.impl.firestore.UserFirestoreDataSourceImpl
import com.example.tuprofe.data.dtos.RegisterUserDto
import com.example.tuprofe.data.dtos.UserDto
import com.google.common.truth.Truth
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class FirebaseUserDataSourceTest {

    private val db = Firebase.firestore
    private lateinit var dataSource: UserFirestoreDataSourceImpl

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
    fun setup() = runTest {
        try {
            db.useEmulator("10.0.2.2", 8080)
        } catch (_: Exception) {}
        dataSource = UserFirestoreDataSourceImpl(db)

        val batch = db.batch()
        repeat(10) { i ->
            val user = generateUser(i)
            batch.set(db.collection("users").document(user.id), user)
        }
        batch.commit().await()
    }

    @After
    fun teardown() = runTest {
        val users = db.collection("users").get().await()
        for (userDoc in users) {
            userDoc.reference.collection("followers").get().await()
                .forEach { it.reference.delete().await() }
            userDoc.reference.collection("following").get().await()
                .forEach { it.reference.delete().await() }
        }
        users.documents.forEach { it.reference.delete().await() }
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
    fun getUserById_invalidId_returnsNull() = runTest {
        val result = dataSource.getUserById("test_user_99")

        Truth.assertThat(result).isNull()
    }

    @Test
    fun registerUser_insertDocument_DocumentExists() = runTest {
        val user = RegisterUserDto(
            username = "testuser10",
            carrera = "Ingeniería de Sistemas",
            FCMToken = "test_token"
        )

        dataSource.registerUser(user, "999")

        val result = dataSource.getUserById("999")
        Truth.assertThat(result).isNotNull()
        Truth.assertThat(result?.id).isEqualTo("999")
        Truth.assertThat(result?.username).isEqualTo(user.username)
        Truth.assertThat(result?.carrera).isEqualTo(user.carrera)
    }

    @Test
    fun updateUser_changesData_dataUpdated() = runTest {
        val user = generateUser(1)
        val newUsername = "updatedUser"
        val newEmail = "updated@test.com"
        val newCarrera = "Ingeniería Civil"

        dataSource.updateUser(user.id, newUsername, newEmail, newCarrera)

        val result = dataSource.getUserById(user.id)
        Truth.assertThat(result?.username).isEqualTo(newUsername)
        Truth.assertThat(result?.email).isEqualTo(newEmail)
        Truth.assertThat(result?.carrera).isEqualTo(newCarrera)
    }

    @Test
    fun followOrUnfollowUser_followUser_UserFollowed() = runTest {
        val currentUser = generateUser(1)
        val targetUser = generateUser(2)

        dataSource.followOrUnfollowUser(currentUser.id, targetUser.id)

        val result = dataSource.getUserById(targetUser.id, currentUser.id)
        Truth.assertThat(result?.followed).isTrue()
    }

    @Test
    fun followOrUnfollowUser_followUser_followersCountIncrement() = runTest {
        val currentUser = generateUser(1)
        val targetUser = generateUser(2)
        val oldData = dataSource.getUserById(targetUser.id)

        dataSource.followOrUnfollowUser(currentUser.id, targetUser.id)

        val result = dataSource.getUserById(targetUser.id)
        Truth.assertThat(result?.followersCount).isGreaterThan(oldData?.followersCount ?: 0)
    }

    @Test
    fun followOrUnfollowUser_unfollow_followedFalse() = runTest {
        val currentUser = generateUser(1)
        val targetUser = generateUser(2)

        dataSource.followOrUnfollowUser(currentUser.id, targetUser.id)
        dataSource.followOrUnfollowUser(currentUser.id, targetUser.id)

        val result = dataSource.getUserById(targetUser.id, currentUser.id)
        Truth.assertThat(result?.followed).isFalse()
    }
}