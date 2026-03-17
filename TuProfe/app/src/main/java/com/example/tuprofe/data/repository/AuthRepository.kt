package com.example.tuprofe.data.repository

import com.example.tuprofe.data.datasource.AuthRemoteDataSource
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource
){

    val currentUser: FirebaseUser? = authRemoteDataSource.currentUser

    suspend fun signIn(email: String, password: String): Result<Unit> {
        return try {
            authRemoteDataSource.signIn(email, password)
            Result.success(Unit)

        } catch (e: FirebaseAuthInvalidCredentialsException) {
            Result.failure(Exception("Credenciales incorrectas"))

        } catch (e: FirebaseAuthInvalidUserException) {
            Result.failure(Exception("Usuario no encontrado"))

        } catch (e: FirebaseTooManyRequestsException) {
            Result.failure(Exception("Demasiados intentos. Intenta más tarde"))

        } catch (e: FirebaseNetworkException) {
            Result.failure(Exception("Error de conexión a internet"))

        } catch (e: Exception) {
            Result.failure(Exception("Error al iniciar sesión"))
        }
    }

    suspend fun signUp(email: String, password: String): Result<Unit> {
        return try {
            authRemoteDataSource.signUp(email, password)
            Result.success(Unit)

        } catch (e: FirebaseAuthWeakPasswordException) {
            Result.failure(Exception("La contraseña es demasiado débil"))

        } catch (e: FirebaseAuthInvalidCredentialsException) {
            Result.failure(Exception("El correo no es válido"))

        } catch (e: FirebaseAuthUserCollisionException) {
            Result.failure(Exception("El usuario ya está registrado"))

        } catch (e: FirebaseTooManyRequestsException) {
            Result.failure(Exception("Demasiados intentos, intenta más tarde"))

        } catch (e: FirebaseNetworkException) {
            Result.failure(Exception("Error de conexión"))

        } catch (e: Exception) {
            Result.failure(Exception("Error al registrar el usuario"))
        }
    }

    fun signOut() {
        authRemoteDataSource.signOut()
    }


}