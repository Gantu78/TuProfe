package com.example.tuprofe.data.datasource

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class StorageRemoteDataSource @Inject constructor(
    private val storage: FirebaseStorage
) {

    suspend fun uploadImage(path: String, image: Uri):String{
        val imageRef = storage.reference.child(path)
        imageRef.putFile(image).await()
        return imageRef.downloadUrl.await().toString()
    }

    suspend fun getProfileImageUrl(userId: String): String? {
        return try {
            storage.reference.child("profileImages/$userId.jpg").downloadUrl.await().toString()
        } catch (e: Exception) {
            null
        }
    }
}