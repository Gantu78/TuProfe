package com.example.tuprofe.data.repository

import android.net.Uri
import com.example.tuprofe.data.datasource.StorageRemoteDataSource
import javax.inject.Inject

class StorageRepository @Inject constructor(
    private val storageRemoteDataSource: StorageRemoteDataSource,
    private val authRepository: AuthRepository
) {

    suspend fun uploadProfileImage(image: Uri): Result<String> {
         return try{
             val userId = authRepository.currentUser?.uid ?: return Result.failure(Exception("Usuario no autenticado"))
             val path = "profileImages/$userId.jpg"
             val url = storageRemoteDataSource.uploadImage(path, image)
             authRepository.updateProfileImage(url)
             Result.success(url)
         }catch(e: Exception){
             Result.failure(Exception("Error al subir la imagen de perfil"))
         }
    }
}