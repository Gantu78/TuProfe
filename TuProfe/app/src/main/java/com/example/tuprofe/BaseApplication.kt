package com.example.tuprofe


import android.app.Application
import android.os.Debug
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.firestore
import com.stripe.android.PaymentConfiguration
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)

        PaymentConfiguration.init(
            applicationContext,
            "pk_test_51Tc03ZFDdteWbxqhknmq029N3rIui1Zn8K8XGFuorgbcdlh0oXVO4uyqQhZRkHPJ9Gwtzjol4YqDqmhx3fHMGTe700IMLwBCNT"
        )
        if (Debug.isDebuggerConnected()) {
            // Disable offline cache to avoid conflicts when switching back to production
            Firebase.firestore.firestoreSettings = FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build()
            Firebase.firestore.useEmulator("10.0.2.2", 8080)
            //Firebase.auth.useEmulator("10.0.2.2", 9099)
        }
    }
}