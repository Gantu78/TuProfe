package com.example.tuprofe.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tuprofe.data.Profesor
import com.example.tuprofe.data.repository.ProfessorRepository
import com.example.tuprofe.data.repository.ReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val professorRepository: ProfessorRepository,
    private val reviewRepository: ReviewRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(SearchState())
    val uiState: StateFlow<SearchState> = _uiState.asStateFlow()

    private var allProfessors: List<Profesor> = emptyList()

    init {
        cargarTodosLosProfesores()
    }

    private fun cargarTodosLosProfesores() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val professorsResult = professorRepository.getProfessors()
            val reviewsResult = reviewRepository.getReviews()

            val ratings = reviewsResult.getOrNull()
                ?.groupBy { it.profesor.profeId }
                ?.mapValues { (_, reviews) -> reviews.map { it.rating }.average().roundToInt() }
                ?: emptyMap()

            professorsResult.onSuccess { list ->
                allProfessors = list
                _uiState.update { it.copy(
                    searchResults = allProfessors,
                    ratings = ratings,
                    isLoading = false
                ) }
            }.onFailure {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onQueryChange(newQuery: String) {
        val results = if (newQuery.isBlank()) {
            allProfessors
        } else {
            allProfessors.filter {
                it.nombreProfe.contains(newQuery, ignoreCase = true)
            }
        }

        _uiState.update { it.copy(
            searchQuery = newQuery,
            searchResults = results
        ) }
    }

}
