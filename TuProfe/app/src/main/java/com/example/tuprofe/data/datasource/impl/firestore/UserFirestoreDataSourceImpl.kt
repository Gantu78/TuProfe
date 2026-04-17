package com.example.tuprofe.data.datasource.impl.firestore

import android.util.Log
import com.example.tuprofe.data.datasource.UserRemoteDataSource
import com.example.tuprofe.data.dtos.RegisterUserDto
import com.example.tuprofe.data.dtos.UserDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserFirestoreDataSourceImpl @Inject constructor(
    private val db: FirebaseFirestore
) : UserRemoteDataSource {

    override suspend fun getUserById(id: String): UserDto {
        val respuesta = db.collection("users").document(id).get().await()
        Log.d("DATA", "Buscando usuario con id: $id | existe: ${respuesta.exists()}")
        return respuesta.toObject(UserDto::class.java)?.copy(id = respuesta.id)
            ?: throw Exception("No se pudo obtener el usuario con id: $id")
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
}