package com.example.tuprofe.ui.Search

import androidx.lifecycle.ViewModel
import com.example.tuprofe.data.local.LocalProfesor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SearchViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SearchState())
    val uiState: StateFlow<SearchState> = _uiState.asStateFlow()

    fun onQueryChange(newQuery: String) {
        val results = if (newQuery.isBlank()) {
            emptyList()
        } else {
            LocalProfesor.profesores.filter {
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
