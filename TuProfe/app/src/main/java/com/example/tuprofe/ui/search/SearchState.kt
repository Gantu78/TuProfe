package com.example.tuprofe.ui.search

import com.example.tuprofe.data.Profesor

data class SearchState(
    val searchQuery: String = "",
    val searchResults: List<Profesor> = emptyList(),
    val isLoading: Boolean = false,
    val navigateToProfileId: Int? = null
)
