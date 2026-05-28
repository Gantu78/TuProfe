package com.example.tuprofe.data.datasource

import android.app.Activity
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.OAuthProvider
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRemoteDataSource @Inject constructor(
    private val auth: FirebaseAuth
) {

    val currentUser: FirebaseUser?
        get() = auth.currentUser

    // ── Email / password ──────────────────────────────────────────────────────

    suspend fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).await()
    }

    suspend fun signUp(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).await()
    }

    fun signOut() {
        auth.signOut()
    }

    suspend fun reauthenticateAndDelete(email: String, password: String) {
        val user = auth.currentUser
        val credential = EmailAuthProvider.getCredential(email, password)
        user?.reauthenticate(credential)?.await()
        user?.delete()?.await()
    }

    suspend fun resetPassword(email: String) {
        auth.sendPasswordResetEmail(email).await()
    }


    suspend fun signInWithGoogleIdToken(idToken: String): FirebaseUser {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        val result = auth.signInWithCredential(credential).await()
        return result.user ?: error("Firebase: usuario nulo tras Google sign-in")
    }

    suspend fun signInWithProvider(activity: Activity, provider: OAuthProvider): FirebaseUser {
        val result = auth.startActivityForSignInWithProvider(activity, provider).await()
        return result.user ?: error("Error: usuario nulo")
    }

    suspend fun signInWithGitHub(activity: Activity): FirebaseUser {
        val provider = OAuthProvider.newBuilder("github.com")
            .setScopes(listOf("user:email"))
            .build()


        val result = auth
            .startActivityForSignInWithProvider(activity, provider)
            .await()

        return result.user ?: error("Firebase: usuario nulo tras GitHub sign-in")
    }
}
