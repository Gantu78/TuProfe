package com.example.tuprofe.ui.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tuprofe.data.Profesor
import com.example.tuprofe.ui.utils.BackgroundImage
import com.example.tuprofe.ui.utils.RatingStars
import com.example.tuprofe.ui.utils.SearchBar
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.res.colorResource
import com.example.tuprofe.R

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    searchViewModel: SearchViewModel = hiltViewModel(),
    onProfessorClick: (String) -> Unit

) {
    val uiState by searchViewModel.uiState.collectAsState()

    SearchContent(
        uiState = uiState,
        onQueryChange = { searchViewModel.onQueryChange(it) },
        onProfessorClick = { onProfessorClick(it.profeId) },
        modifier = modifier
    )
}

@Composable
fun SearchContent(
    uiState: SearchState,
    onQueryChange: (String) -> Unit,
    onProfessorClick: (Profesor) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        BackgroundImage()

        Column {
            Spacer(modifier = Modifier.height(20.dp))

            SearchBar(
                query = uiState.searchQuery,
                onQueryChange = onQueryChange,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.searchResults) { profesor ->
                        ProfessorSearchResultItem(
                            profesor = profesor,
                            rating = uiState.ratings[profesor.profeId] ?: 0,
                            onClick = { onProfessorClick(profesor) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProfessorSearchResultItem(profesor: Profesor, rating: Int, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = profesor.nombreProfe,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            RatingStars(rating = rating, starColor = colorResource(R.color.verdetp))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SearchScreenPreview() {
    SearchContent(
        uiState = SearchState(),
        onQueryChange = {},
        onProfessorClick = {}
    )
}
