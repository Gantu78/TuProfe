package com.example.tuprofe.ui.Search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tuprofe.data.Profesor
import com.example.tuprofe.data.datasource.Services.ReviewRetrofitService
import com.example.tuprofe.data.dtos.toProfesor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val service: ReviewRetrofitService
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
            try {
                // Usando el endpoint /professors del backend
                val professorsDto = service.getAllProfessors()
                allProfessors = professorsDto.map { it.toProfesor() }

                _uiState.update { it.copy(
                    searchResults = allProfessors,
                    isLoading = false
                ) }
            } catch (e: Exception) {
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

    fun onProfileClick(profeId: Int) {
        _uiState.update { it.copy(navigateToProfileId = profeId) }
    }

    fun onNavigationHandled() {
        _uiState.update { it.copy(navigateToProfileId = null) }
    }
}
