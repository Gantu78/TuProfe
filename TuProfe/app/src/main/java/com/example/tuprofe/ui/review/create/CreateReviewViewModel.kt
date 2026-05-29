package com.example.tuprofe.ui.review.create

import android.net.Uri
import com.example.tuprofe.R
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
                    error = R.string.error_al_cargar_datos,
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
            _uiState.update { it.copy(error = R.string.debes_seleccionar_profesor) }
            return
        }

        if (currentState.selectedMateria.isBlank()) {
            _uiState.update { it.copy(error = R.string.debes_seleccionar_materia) }
            return
        }

        if (currentState.rating < 1 || currentState.rating > 5) {
            _uiState.update { it.copy(error = R.string.calificacion_entre_1_y_5) }
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
                        it.copy(isLoading = false, error = R.string.error_al_subir_imagenes)
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
                latitude = if (currentState.includeLocation) currentState.latitude else null,
                longitude = if (currentState.includeLocation) currentState.longitude else null,
                imageUrls = imageUrls
            )

            if (result.isSuccess) {
                _uiState.update { it.copy(isLoading = false, success = true) }
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = R.string.error_al_publicar
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

    fun onClearProfessor() {
        _uiState.update { it.copy(
            selectedProfessor = null,
            professorQuery = "",
            selectedMateria = "",
            isMateriaDropdownExpanded = false
        ) }
    }

    fun onToggleIncludeLocation(enabled: Boolean) {
        _uiState.update { it.copy(
            includeLocation = enabled,
            latitude = if (!enabled) null else it.latitude,
            longitude = if (!enabled) null else it.longitude
        ) }
    }
}