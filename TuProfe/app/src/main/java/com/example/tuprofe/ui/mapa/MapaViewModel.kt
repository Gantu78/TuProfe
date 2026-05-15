package com.example.tuprofe.ui.mapa

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tuprofe.data.repository.ReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapaViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MapaState())
    val uiState: StateFlow<MapaState> = _uiState.asStateFlow()

    init {
        loadMarkers()
    }

    fun loadMarkers() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            reviewRepository.getMapMarkers().fold(
                onSuccess = { markers ->
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            markers = markers,
                            filteredMarkers = applyFiltersTo(
                                markers, state.filterStars, state.filterProfesores, state.filterMaterias
                            )
                        )
                    }
                },
                onFailure = { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                }
            )
        }
    }

    fun onMarkerSelected(marker: ReviewMapMarker) {
        _uiState.update { it.copy(selectedMarker = marker, selectedGroup = null) }
    }

    fun onGroupSelected(markers: List<ReviewMapMarker>) {
        _uiState.update { it.copy(selectedGroup = markers, selectedMarker = null) }
    }

    fun onDismissMarker() {
        _uiState.update { it.copy(selectedMarker = null, selectedGroup = null) }
    }

    fun toggleReviewList() {
        _uiState.update { it.copy(showReviewList = !it.showReviewList) }
    }

    fun onReviewListItemClick(marker: ReviewMapMarker) {
        _uiState.update {
            it.copy(
                showReviewList = false,
                selectedMarker = marker,
                navigateToMarker = marker
            )
        }
    }

    fun onNavigationConsumed() {
        _uiState.update { it.copy(navigateToMarker = null) }
    }

    fun toggleFilterPanel() {
        _uiState.update { it.copy(showFilterPanel = !it.showFilterPanel) }
    }

    fun toggleStarFilter(star: Int) {
        _uiState.update { state ->
            val newStars = if (star in state.filterStars) state.filterStars - star else state.filterStars + star
            state.copy(
                filterStars = newStars,
                filteredMarkers = applyFiltersTo(state.markers, newStars, state.filterProfesores, state.filterMaterias)
            )
        }
    }

    fun toggleProfesorFilter(profesor: String) {
        _uiState.update { state ->
            val newProfesores = if (profesor in state.filterProfesores) state.filterProfesores - profesor else state.filterProfesores + profesor
            state.copy(
                filterProfesores = newProfesores,
                filteredMarkers = applyFiltersTo(state.markers, state.filterStars, newProfesores, state.filterMaterias)
            )
        }
    }

    fun toggleMateriaFilter(materia: String) {
        _uiState.update { state ->
            val newMaterias = if (materia in state.filterMaterias) state.filterMaterias - materia else state.filterMaterias + materia
            state.copy(
                filterMaterias = newMaterias,
                filteredMarkers = applyFiltersTo(state.markers, state.filterStars, state.filterProfesores, newMaterias)
            )
        }
    }

    fun clearFilters() {
        _uiState.update { state ->
            state.copy(
                filterStars = emptySet(),
                filterProfesores = emptySet(),
                filterMaterias = emptySet(),
                filteredMarkers = state.markers
            )
        }
    }

    private fun applyFiltersTo(
        markers: List<ReviewMapMarker>,
        stars: Set<Int>,
        profesores: Set<String>,
        materias: Set<String>
    ): List<ReviewMapMarker> = markers.filter { marker ->
        (stars.isEmpty() || marker.rating in stars) &&
        (profesores.isEmpty() || marker.profesorNombre in profesores) &&
        (materias.isEmpty() || marker.materia in materias)
    }
}