package com.example.tuprofe.ui.review.create

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tuprofe.data.Profesor
import com.example.tuprofe.data.repository.AuthRepository
import com.example.tuprofe.data.repository.ProfessorRepository
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
class CreateReviewViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository,
    private val professorRepository: ProfessorRepository,
    private val authRepository: AuthRepository,
    private val storageRepository: StorageRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateReviewState())
    val uiState: StateFlow<CreateReviewState> = _uiState.asStateFlow()

    init {
        cargarProfesores()
    }

    fun cargarProfesores() {
        _uiState.update { it.copy(isSearchingProfessors = true) }
        viewModelScope.launch {
            professorRepository.getProfessors().onSuccess { list ->
                _uiState.update { it.copy(
                    professors = list,
                    filteredProfessors = list,
                    isSearchingProfessors = false
                ) }
            }.onFailure { e ->
                _uiState.update { it.copy(
                    error = e.message,
                    isSearchingProfessors = false
                ) }
            }
        }
    }

    fun onReviewTextChange(newText: String) {
        _uiState.update { it.copy(reviewText = newText) }
    }

    fun onProfessorQueryChange(query: String) {
        _uiState.update { state ->
            val filtered = if (query.isBlank()) {
                state.professors
            } else {
                state.professors.filter {
                    it.nombreProfe.contains(query, ignoreCase = true)
                }
            }
            state.copy(
                professorQuery = query,
                filteredProfessors = filtered,
                isDropdownExpanded = query.isNotBlank() && filtered.isNotEmpty() && state.selectedProfessor?.nombreProfe != query
            )
        }
    }

    fun onProfessorSelected(professor: Profesor) {
        _uiState.update { it.copy(
            selectedProfessor = professor,
            professorQuery = professor.nombreProfe,
            isDropdownExpanded = false,
            selectedMateria = "",
            isMateriaDropdownExpanded = false
        ) }
    }

    fun onMateriaSelected(materia: String) {
        _uiState.update { it.copy(
            selectedMateria = materia,
            isMateriaDropdownExpanded = false
        ) }
    }

    fun toggleMateriaDropdown() {
        _uiState.update { it.copy(isMateriaDropdownExpanded = !it.isMateriaDropdownExpanded) }
    }

    fun onDismissMateriaDropdown() {
        _uiState.update { it.copy(isMateriaDropdownExpanded = false) }
    }

    fun onRatingChange(newRating: Int) {
        _uiState.update { it.copy(rating = newRating) }
    }

    fun onLocationReceived(latitude: Double?, longitude: Double?) {
        _uiState.update { it.copy(latitude = latitude, longitude = longitude) }
    }

    fun onImagesSelected(uris: List<Uri>) {
        val current = _uiState.value.selectedImageUris
        val combined = (current + uris).distinct().take(4)
        _uiState.update { it.copy(selectedImageUris = combined) }
    }

    fun onRemoveImage(index: Int) {
        val updated = _uiState.value.selectedImageUris.toMutableList().also { it.removeAt(index) }
        _uiState.update { it.copy(selectedImageUris = updated) }
    }

    fun createReview() {
        val currentState = _uiState.value
        val professorId = currentState.selectedProfessor?.profeId ?: ""

        if (professorId.isBlank()) {
            _uiState.update { it.copy(error = "Debes seleccionar un profesor") }
            return
        }

        if (currentState.selectedMateria.isBlank()) {
            _uiState.update { it.copy(error = "Debes seleccionar una materia") }
            return
        }

        if (currentState.rating < 1 || currentState.rating > 5) {
            _uiState.update { it.copy(error = "La calificación debe estar entre 1 y 5") }
            return
        }

        _uiState.update { it.copy(isLoading = true, error = null) }

        val userId = authRepository.currentUser?.uid ?: return

        viewModelScope.launch {
            val imageUrls = if (currentState.selectedImageUris.isNotEmpty()) {
                _uiState.update { it.copy(isUploadingImages = true) }
                val uploadResult = storageRepository.uploadReviewImages(currentState.selectedImageUris)
                _uiState.update { it.copy(isUploadingImages = false) }
                if (uploadResult.isFailure) {
                    _uiState.update {
                        it.copy(isLoading = false, error = uploadResult.exceptionOrNull()?.message ?: "Error al subir imágenes")
                    }
                    return@launch
                }
                uploadResult.getOrDefault(emptyList())
            } else emptyList()

            val result = reviewRepository.createReview(
                userId = userId,
                professorId = professorId,
                content = currentState.reviewText,
                rating = currentState.rating,
                materia = currentState.selectedMateria,
                latitude = currentState.latitude,
                longitude = currentState.longitude,
                imageUrls = imageUrls
            )

            if (result.isSuccess) {
                _uiState.update { it.copy(isLoading = false, success = true) }
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = result.exceptionOrNull()?.message ?: "Error desconocido"
                    )
                }
            }
        }
    }

    fun resetSuccess() {
        // En lugar de solo resetear success, reseteamos el estado entero para la próxima vez
        _uiState.update { CreateReviewState() }
        cargarProfesores()
    }

    fun onDismissDropdown() {
        _uiState.update { it.copy(isDropdownExpanded = false) }
    }

    fun toggleDropdown() {
        _uiState.update { it.copy(isDropdownExpanded = !it.isDropdownExpanded) }
    }
}