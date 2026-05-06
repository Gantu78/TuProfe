package com.example.tuprofe.repository

import com.example.tuprofe.data.datasource.StorageRemoteDataSource
import com.example.tuprofe.data.datasource.impl.firestore.UserFirestoreDataSourceImpl
import com.example.tuprofe.data.dtos.UserDto
import com.example.tuprofe.data.repository.UserRepository
import com.google.common.truth.Truth
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class UserRepositoryIntegrationTest {

    private val db = Firebase.firestore
    private val storage = Firebase.storage
    private lateinit var dataSource: UserFirestoreDataSourceImpl
    private lateinit var userRepository: UserRepository

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
            storage.useEmulator("10.0.2.2", 9199)
        } catch (_: Exception) {}
        dataSource = UserFirestoreDataSourceImpl(db)
        userRepository = UserRepository(dataSource, StorageRemoteDataSource(storage))

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
        val id = "test_user_9"
        val expectedUsername = "testuser9"

        val result = userRepository.getUserById(id)

        Truth.assertThat(result.isSuccess).isTrue()
        Truth.assertThat(result.getOrNull()?.nombreUsu).isEqualTo(expectedUsername)
    }

    @Test
    fun getUserById_invalidId_returnsFailure() = runTest {
        val result = userRepository.getUserById("test_user_99")

        Truth.assertThat(result.isFailure).isTrue()
    }

    @Test
    fun updateUser_changesData_fieldsAreUpdated() = runTest {
        val user = generateUser(1)
        val newUsername = "updatedUser"
        val newEmail = "updated@test.com"
        val newCarrera = "Ingeniería Civil"

        userRepository.updateUser(user.id, newUsername, newEmail, newCarrera)

        val result = userRepository.getUserById(user.id)
        Truth.assertThat(result.isSuccess).isTrue()
        Truth.assertThat(result.getOrNull()?.nombreUsu).isEqualTo(newUsername)
        Truth.assertThat(result.getOrNull()?.email).isEqualTo(newEmail)
        Truth.assertThat(result.getOrNull()?.carrera).isEqualTo(newCarrera)
    }

    @Test
    fun followOrUnfollow_follow_returnsSuccess() = runTest {
        val currentUser = generateUser(1)
        val targetUser = generateUser(2)

        val result = userRepository.followOrUnfollow(currentUser.id, targetUser.id)

        Truth.assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun followOrUnfollow_followUser_targetIsFollowed() = runTest {
        val currentUser = generateUser(1)
        val targetUser = generateUser(2)

        userRepository.followOrUnfollow(currentUser.id, targetUser.id)

        val result = userRepository.getUserById(targetUser.id, currentUser.id)
        Truth.assertThat(result.isSuccess).isTrue()
        Truth.assertThat(result.getOrNull()?.followed).isTrue()
    }

    @Test
    fun followOrUnfollow_unfollowUser_targetIsNotFollowed() = runTest {
        val currentUser = generateUser(1)
        val targetUser = generateUser(2)

        userRepository.followOrUnfollow(currentUser.id, targetUser.id)
        userRepository.followOrUnfollow(currentUser.id, targetUser.id)

        val result = userRepository.getUserById(targetUser.id, currentUser.id)
        Truth.assertThat(result.isSuccess).isTrue()
        Truth.assertThat(result.getOrNull()?.followed).isFalse()
    }

    @Test
    fun getFollowers_afterFollow_listContainsFollower() = runTest {
        val currentUser = generateUser(1)
        val targetUser = generateUser(2)
        userRepository.followOrUnfollow(currentUser.id, targetUser.id)

        val result = userRepository.getFollowers(targetUser.id, currentUser.id)

        Truth.assertThat(result.isSuccess).isTrue()
        Truth.assertThat(result.getOrNull()?.map { it.usuarioId }).contains(currentUser.id)
    }

    @Test
    fun getFollowing_afterFollow_listContainsTarget() = runTest {
        val currentUser = generateUser(1)
        val targetUser = generateUser(2)
        userRepository.followOrUnfollow(currentUser.id, targetUser.id)

        val result = userRepository.getFollowing(currentUser.id, currentUser.id)

        Truth.assertThat(result.isSuccess).isTrue()
        Truth.assertThat(result.getOrNull()?.map { it.usuarioId }).contains(targetUser.id)
    }

    @Test
    fun getFollowingIds_afterFollow_containsTargetId() = runTest {
        val currentUser = generateUser(1)
        val targetUser = generateUser(2)
        userRepository.followOrUnfollow(currentUser.id, targetUser.id)

        val result = userRepository.getFollowingIds(currentUser.id)

        Truth.assertThat(result.isSuccess).isTrue()
        Truth.assertThat(result.getOrNull()).contains(targetUser.id)
    }
}