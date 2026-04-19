package com.example.tuprofe.data.datasource.impl.firestore

import android.util.Log
import com.example.tuprofe.data.datasource.UserRemoteDataSource
import com.example.tuprofe.data.dtos.RegisterUserDto
import com.example.tuprofe.data.dtos.UserDto
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserFirestoreDataSourceImpl @Inject constructor(
    private val db: FirebaseFirestore
) : UserRemoteDataSource {

    override suspend fun getUserById(id: String, currentUserId: String): UserDto {
        val docRef = db.collection("users").document(id)
        val respuesta = docRef.get().await()
        val user =  respuesta.toObject(UserDto::class.java)?: throw Exception("No se pudo obtener el usuario con id: $id")
        user.copy(id = id)
        if(currentUserId.isNotEmpty()){
            val followerDoc = db.collection("users").document(id).collection("followers").document(currentUserId).get().await()
            val exist = followerDoc.exists()
            user.followed = exist
        }
        return user

    }

    override suspend fun registerUser(registerUserDto: RegisterUserDto, userID: String) {
        db.collection("users").document(userID).set(registerUserDto).await()
    }

    override suspend fun updateUser(userId: String, username: String, email: String, carrera: String) {
        val updates = mapOf(
            "username" to username,
            "email" to email,
            "carrera" to carrera
        )
        db.collection("users").document(userId).update(updates).await()
    }

    override suspend fun updateUserPhoto(userId: String, photoUrl: String) {
        db.collection("users").document(userId).update("foto", photoUrl).await()
    }


    override suspend fun followOrUnfollowUser(currentUserId: String, targetUserId: String) {
        Log.d("TEST", "currentUserId: $currentUserId")
        val currentUserRef = db.collection("users").document(currentUserId)
        val targetUserRef = db.collection("users").document(targetUserId)

        val followingRef = currentUserRef.collection("following").document(targetUserId)
        val followerRef = targetUserRef.collection("followers").document(currentUserId)

        db.runTransaction { transaction ->
            val followingDoc = transaction.get(followingRef)

            if (followingDoc.exists()) {
                transaction.delete(followingRef)
                transaction.delete(followerRef)
                transaction.update(currentUserRef, "followingCount", FieldValue.increment(-1))
                transaction.update(targetUserRef, "followersCount", FieldValue.increment(-1))
            } else {
                transaction.set(followingRef, mapOf("timestamp" to FieldValue.serverTimestamp()))
                transaction.set(followerRef, mapOf("timestamp" to FieldValue.serverTimestamp()))
                transaction.update(currentUserRef, "followingCount", FieldValue.increment(1))
                transaction.update(targetUserRef, "followersCount", FieldValue.increment(1))
            }
        }.await()
    }


}