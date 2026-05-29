package com.example.tuprofe.ui.review.edit

import android.net.Uri
import androidx.annotation.StringRes

data class EditReviewState(
    val reviewId: String = "",
    val reviewText: String = "",
    val professorName: String = "",
    val rating: Int = 0,
    val isLoading: Boolean = false,
    val isInitialLoading: Boolean = true,
    val isUploadingImages: Boolean = false,
    val success: Boolean = false,
    @StringRes val error: Int? = null,
    val existingImageUrls: List<String> = emptyList(),
    val newImageUris: List<Uri> = emptyList(),
    val includeLocation: Boolean = false,
    val latitude: Double? = null,
    val longitude: Double? = null,
)
