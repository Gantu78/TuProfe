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
        val state = _uiState.value
        fetchMarkers(state.filterStars, state.filterProfesores, state.filterMaterias)
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
        val state = _uiState.value
        val newStars = if (star in state.filterStars) state.filterStars - star else state.filterStars + star
        _uiState.update { it.copy(filterStars = newStars) }
        fetchMarkers(newStars, state.filterProfesores, state.filterMaterias)
    }

    fun toggleProfesorFilter(profesor: String) {
        val state = _uiState.value
        val newProfesores = if (profesor in state.filterProfesores) state.filterProfesores - profesor else state.filterProfesores + profesor
        _uiState.update { it.copy(filterProfesores = newProfesores) }
        fetchMarkers(state.filterStars, newProfesores, state.filterMaterias)
    }

    fun toggleMateriaFilter(materia: String) {
        val state = _uiState.value
        val newMaterias = if (materia in state.filterMaterias) state.filterMaterias - materia else state.filterMaterias + materia
        _uiState.update { it.copy(filterMaterias = newMaterias) }
        fetchMarkers(state.filterStars, state.filterProfesores, newMaterias)
    }

    fun clearFilters() {
        _uiState.update { it.copy(filterStars = emptySet(), filterProfesores = emptySet(), filterMaterias = emptySet()) }
        fetchMarkers(emptySet(), emptySet(), emptySet())
    }

    private fun fetchMarkers(
        stars: Set<Int>,
        profesores: Set<String>,
        materias: Set<String>
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            reviewRepository.getMapMarkers(stars, profesores, materias).fold(
                onSuccess = { markers ->
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            markers = markers,
                            // Solo actualiza el pool completo cuando no hay filtros activos
                            allMarkers = if (stars.isEmpty() && profesores.isEmpty() && materias.isEmpty())
                                markers else state.allMarkers
                        )
                    }
                },
                onFailure = { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                }
            )
        }
    }
}
