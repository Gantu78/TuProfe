package com.example.tuprofe


import android.app.Application
import android.os.Debug
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.firestore
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)

        if (Debug.isDebuggerConnected()) {
            // Disable offline cache to avoid conflicts when switching back to production
            Firebase.firestore.firestoreSettings = FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build()
            Firebase.firestore.useEmulator("10.0.2.2", 8080)
            Firebase.auth.useEmulator("10.0.2.2", 9099)
        }
    }
}