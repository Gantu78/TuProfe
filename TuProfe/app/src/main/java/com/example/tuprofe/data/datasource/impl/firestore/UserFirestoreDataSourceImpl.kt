package com.example.tuprofe.data.datasource.impl.firestore

import android.util.Log
import com.example.tuprofe.data.datasource.UsuarioRemoteDataSource
import com.example.tuprofe.data.dtos.RegisterUserDto
import com.example.tuprofe.data.dtos.UserDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserFirestoreDataSourceImpl @Inject constructor(
    private val db: FirebaseFirestore
) : UsuarioRemoteDataSource {

    override suspend fun getUserById(id: String): UserDto {
        val respuesta = db.collection("users").document(id).get().await()
        Log.d("DATA", "Buscando usuario con id: $id | existe: ${respuesta.exists()}")
        return respuesta.toObject(UserDto::class.java)?.copy(id = respuesta.id)
            ?: throw Exception("No se pudo obtener el usuario con id: $id")
    }

    override suspend fun registerUser(registerUserDto: RegisterUserDto, userID: String) {
        db.collection("users").document(userID).set(registerUserDto).await()
    }
}