package com.example.tuprofe.data.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class LocationService @Inject constructor(
    @ApplicationContext private val context: Context,
    private val fusedLocationClient: FusedLocationProviderClient
) {
    suspend fun getCurrentLocation(): Pair<Double, Double>? {
        val hasFine = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val hasCoarse = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasFine && !hasCoarse) return null

        return try {
            val priority = if (hasFine)
                Priority.PRIORITY_HIGH_ACCURACY
            else
                Priority.PRIORITY_BALANCED_POWER_ACCURACY

            val request = CurrentLocationRequest.Builder()
                .setPriority(priority)
                .setMaxUpdateAgeMillis(30_000L)
                .build()

            val location = fusedLocationClient
                .getCurrentLocation(request, null)
                .await()

            location?.let { Pair(it.latitude, it.longitude) }
        } catch (e: Exception) {
            null
        }
    }
}
