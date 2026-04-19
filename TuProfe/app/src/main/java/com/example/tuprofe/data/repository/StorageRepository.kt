package com.example.tuprofe.data.repository

import android.net.Uri
import android.util.Log
import com.example.tuprofe.data.datasource.StorageRemoteDataSource
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class StorageRepository @Inject constructor(
    private val storageRemoteDataSource: StorageRemoteDataSource,
    private val authRepository: AuthRepository
) {

    suspend fun uploadProfileImage(image: Uri): Result<String> {
        return try {
            val userId = authRepository.currentUser?.uid
                ?: return Result.failure(Exception("Usuario no autenticado"))

            val path = "profileImages/$userId.jpg"

            val url = storageRemoteDataSource.uploadImage(path, image)

            authRepository.updateProfileImage(url)

            Result.success(url)

        } catch (e: java.io.IOException) {
            Result.failure(Exception("Error de conexión. Verifica tu internet"))

        } catch (e: com.google.firebase.storage.StorageException) {
            Result.failure(Exception("Error en Firebase Storage: ${e.message}"))

        } catch (e: SecurityException) {
            Result.failure(Exception("Permisos insuficientes para acceder a la imagen"))

        } catch (e: IllegalArgumentException) {
            Result.failure(Exception("La imagen seleccionada no es válida"))

        } catch (e: Exception) {
            Result.failure(Exception(e.message ?: "Error desconocido al subir imagen"))
        }
    }
}