package com.example.tuprofe.ui.mapa

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tuprofe.data.ReviewMapMarker
import com.example.tuprofe.data.repository.ModerationCache
import com.example.tuprofe.data.repository.ModerationRepository
import com.example.tuprofe.data.repository.applyModerationFilter
import com.example.tuprofe.data.repository.ReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapaViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository,
    private val moderationRepository: ModerationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MapaState())
    val uiState: StateFlow<MapaState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            ModerationCache.updateEvent.collect {
                _uiState.update { state ->
                    state.copy(markers = state.allMarkers.applyModerationFilter())
                }
            }
        }
        loadMarkers()
    }

    fun loadMarkers() {
        val s = _uiState.value
        fetchMarkers(s.filterStars, s.filterProfesores, s.filterMaterias)
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
            it.copy(showReviewList = false, selectedMarker = marker, navigateToMarker = marker)
        }
    }

    fun onNavigationConsumed() {
        _uiState.update { it.copy(navigateToMarker = null) }
    }

    fun toggleFilterPanel() {
        _uiState.update { it.copy(showFilterPanel = !it.showFilterPanel) }
    }

    fun toggleStarFilter(star: Int) {
        val s = _uiState.updateAndGet { state ->
            val newStars = if (star in state.filterStars) state.filterStars - star else state.filterStars + star
            state.copy(filterStars = newStars)
        }
        fetchMarkers(s.filterStars, s.filterProfesores, s.filterMaterias)
    }

    fun toggleProfesorFilter(profesor: String) {
        val s = _uiState.updateAndGet { state ->
            val newProfesores = if (profesor in state.filterProfesores) state.filterProfesores - profesor else state.filterProfesores + profesor
            state.copy(filterProfesores = newProfesores)
        }
        fetchMarkers(s.filterStars, s.filterProfesores, s.filterMaterias)
    }

    fun toggleMateriaFilter(materia: String) {
        val s = _uiState.updateAndGet { state ->
            val newMaterias = if (materia in state.filterMaterias) state.filterMaterias - materia else state.filterMaterias + materia
            state.copy(filterMaterias = newMaterias)
        }
        fetchMarkers(s.filterStars, s.filterProfesores, s.filterMaterias)
    }

    fun clearFilters() {
        _uiState.update { it.copy(filterStars = emptySet(), filterProfesores = emptySet(), filterMaterias = emptySet()) }
        fetchMarkers(emptySet(), emptySet(), emptySet())
    }

    private fun fetchMarkers(stars: Set<Int>, profesores: Set<String>, materias: Set<String>) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            reviewRepository.getMapMarkers(stars, profesores, materias).fold(
                onSuccess = { rawMarkers ->
                    val markers = rawMarkers.applyModerationFilter()
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            markers = markers,
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
