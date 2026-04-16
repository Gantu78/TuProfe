package com.example.tuprofe.data.datasource.impl.firestore

import com.example.tuprofe.data.datasource.UsuarioRemoteDataSource
import com.example.tuprofe.data.dtos.RegisterUserDto
import com.example.tuprofe.data.dtos.UserDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserFirestoreDataSourceImpl @Inject constructor(private val db: FirebaseFirestore): UsuarioRemoteDataSource {
    override suspend fun getUserById(id: String): UserDto {
        TODO("Not yet implemented")
    }
    override suspend fun registerUser(registerUserDto: RegisterUserDto, userID: String) {
        val docRef = db.collection("users").document(userID)
        docRef.set(registerUserDto).await()
    }


}