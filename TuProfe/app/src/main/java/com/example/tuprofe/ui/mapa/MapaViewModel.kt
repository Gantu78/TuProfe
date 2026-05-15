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
                    _uiState.update { it.copy(isLoading = false, markers = markers) }
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
}
