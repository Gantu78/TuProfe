package com.example.tuprofe.ui.review.edit

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tuprofe.R
import com.example.tuprofe.data.repository.ReviewRepository
import com.example.tuprofe.data.repository.StorageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditReviewViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository,
    private val storageRepository: StorageRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditReviewState())
    val uiState: StateFlow<EditReviewState> = _uiState.asStateFlow()

    init {
        val reviewId = savedStateHandle.get<String>("reviewId") ?: ""
        if (reviewId.isNotEmpty()) cargarReview(reviewId)
    }

    private fun cargarReview(reviewId: String) {
        _uiState.update { it.copy(reviewId = reviewId, isInitialLoading = true) }
        viewModelScope.launch {
            val result = reviewRepository.getReviewById(reviewId)
            if (result.isSuccess) {
                val review = result.getOrNull()
                if (review != null) {
                    _uiState.update {
                        it.copy(
                            reviewText = review.content,
                            rating = review.rating,
                            professorName = review.profesor.nombreProfe,
                            existingImageUrls = review.imageUrls,
                            includeLocation = review.latitude != null && review.longitude != null,
                            latitude = review.latitude,
                            longitude = review.longitude,
                            isInitialLoading = false
                        )
                    }
                } else {
                    _uiState.update { it.copy(error = R.string.rese_a_no_encontrada, isInitialLoading = false) }
                }
            } else {
                _uiState.update { it.copy(error = R.string.error_al_cargar_datos, isInitialLoading = false) }
            }
        }
    }

    fun onReviewTextChange(text: String) = _uiState.update { it.copy(reviewText = text) }

    fun onRatingChange(rating: Int) = _uiState.update { it.copy(rating = rating) }

    fun onImagesSelected(uris: List<Uri>) {
        val state = _uiState.value
        val remaining = 4 - state.existingImageUrls.size - state.newImageUris.size
        val toAdd = uris.distinct().take(remaining.coerceAtLeast(0))
        _uiState.update { it.copy(newImageUris = it.newImageUris + toAdd) }
    }

    fun onRemoveExistingImage(url: String) {
        _uiState.update { it.copy(existingImageUrls = it.existingImageUrls.filter { u -> u != url }) }
    }

    fun onRemoveNewImage(index: Int) {
        _uiState.update {
            it.copy(newImageUris = it.newImageUris.toMutableList().also { list -> list.removeAt(index) })
        }
    }

    fun onToggleIncludeLocation(enabled: Boolean) {
        _uiState.update {
            it.copy(
                includeLocation = enabled,
                latitude = if (!enabled) null else it.latitude,
                longitude = if (!enabled) null else it.longitude
            )
        }
    }

    fun onLocationReceived(latitude: Double?, longitude: Double?) {
        _uiState.update { it.copy(latitude = latitude, longitude = longitude) }
    }

    fun updateReview() {
        val state = _uiState.value
        if (state.reviewText.isBlank()) {
            _uiState.update { it.copy(error = R.string.contenido_no_puede_vacio) }
            return
        }
        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            val newUrls = if (state.newImageUris.isNotEmpty()) {
                _uiState.update { it.copy(isUploadingImages = true) }
                val uploadResult = storageRepository.uploadReviewImages(state.newImageUris)
                _uiState.update { it.copy(isUploadingImages = false) }
                if (uploadResult.isFailure) {
                    _uiState.update { it.copy(isLoading = false, error = R.string.error_al_subir_imagenes) }
                    return@launch
                }
                uploadResult.getOrDefault(emptyList())
            } else emptyList()

            val allImageUrls = state.existingImageUrls + newUrls

            val result = reviewRepository.updateReview(
                reviewId = state.reviewId,
                content = state.reviewText,
                rating = state.rating,
                imageUrls = allImageUrls,
                latitude = if (state.includeLocation) state.latitude else null,
                longitude = if (state.includeLocation) state.longitude else null
            )

            if (result.isSuccess) {
                _uiState.update { it.copy(isLoading = false, success = true) }
            } else {
                _uiState.update { it.copy(isLoading = false, error = R.string.error_al_actualizar) }
            }
        }
    }
}
