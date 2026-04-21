package com.example.tuprofe.ui.search

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.tuprofe.R
import com.example.tuprofe.data.Profesor
import com.example.tuprofe.ui.utils.AnimatedListItem
import com.example.tuprofe.ui.utils.BackgroundImage
import com.example.tuprofe.ui.utils.ProfessorListSkeleton
import com.example.tuprofe.ui.utils.RatingStars
import com.example.tuprofe.ui.utils.SearchBar
import com.example.tuprofe.ui.utils.pressScaleEffect

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    searchViewModel: SearchViewModel = hiltViewModel(),
    onProfessorClick: (String) -> Unit
) {
    val uiState by searchViewModel.uiState.collectAsState()

    SearchContent(
        uiState = uiState,
        onQueryChange = searchViewModel::onQueryChange,
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

        Column(modifier = Modifier.fillMaxSize()) {
            SearchHeader(
                query = uiState.searchQuery,
                onQueryChange = onQueryChange
            )

            when {
                uiState.isLoading -> {
                    // Shimmer placeholders while data loads
                    ProfessorListSkeleton(count = 5)
                }
                uiState.searchResults.isEmpty() -> SearchEmptyState(query = uiState.searchQuery)
                else -> ProfessorList(
                    professors = uiState.searchResults,
                    ratings = uiState.ratings,
                    onProfessorClick = onProfessorClick
                )
            }
        }
    }
}

@Composable
private fun SearchHeader(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SearchBar(
            query = query,
            onQueryChange = onQueryChange
        )
    }
}

@Composable
private fun ProfessorList(
    professors: List<Profesor>,
    ratings: Map<String, Float>,
    onProfessorClick: (Profesor) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            SearchResultsCount(count = professors.size)
        }
        itemsIndexed(
            professors,
            key = { _, p -> p.profeId }
        ) { index, profesor ->
            AnimatedListItem(index = index) {
                ProfessorCard(
                    profesor = profesor,
                    rating = ratings[profesor.profeId] ?: 0f,
                    onClick = { onProfessorClick(profesor) }
                )
            }
        }
    }
}

@Composable
private fun SearchResultsCount(count: Int, modifier: Modifier = Modifier) {
    val label = if (count == 1) "1 profesor encontrado" else "$count profesores encontrados"
    Text(
        text = label,
        fontSize = 13.sp,
        color = colorResource(R.color.gris),
        modifier = modifier.padding(start = 4.dp, bottom = 4.dp)
    )
}

@Composable
fun ProfessorCard(
    profesor: Profesor,
    rating: Float,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .pressScaleEffect(),                    // ← scale on press
        shape = RoundedCornerShape(28.dp),
        border = BorderStroke(2.dp, colorResource(R.color.BordeTuProfe)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            ProfessorAvatar(imageUrl = profesor.imageprofeUrl)
            ProfessorInfo(
                name = profesor.nombreProfe,
                department = profesor.departamento,
                rating = rating,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun ProfessorAvatar(imageUrl: String?, modifier: Modifier = Modifier) {
    AsyncImage(
        model = imageUrl,
        contentDescription = null,
        placeholder = painterResource(R.drawable.loading_img),
        error = painterResource(R.drawable.avatar),
        contentScale = ContentScale.Crop,
        modifier = modifier
            .size(56.dp)
            .shadow(4.dp, CircleShape, clip = false)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .border(1.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f), CircleShape)
    )
}

@Composable
private fun ProfessorInfo(
    name: String,
    department: String,
    rating: Float,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = name,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onSurface
        )
        if (department.isNotBlank()) {
            Text(
                text = department,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        RatingStars(
            rating = rating,
            starSize = 18.dp,
            starColor = colorResource(R.color.verdetp)
        )
    }
}

@Composable
private fun SearchLoadingState(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = colorResource(R.color.verdetp))
    }
}

@Composable
private fun SearchEmptyState(query: String, modifier: Modifier = Modifier) {
    val message = if (query.isBlank()) {
        "Busca a tu profe favorito"
    } else {
        "No se encontraron resultados\npara \"$query\""
    }
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = colorResource(R.color.gris),
                modifier = Modifier.size(64.dp)
            )
            Text(
                text = message,
                fontSize = 15.sp,
                color = colorResource(R.color.gris),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SearchScreenPreview() {
    SearchContent(
        uiState = SearchState(
            searchResults = listOf(
                Profesor("1", "Carlos Rodríguez", null, "Matemáticas"),
                Profesor("2", "Ana María García", null, "Física Cuántica"),
                Profesor("3", "Luis Herrera", null, "")
            ),
            ratings = mapOf("1" to 4.5f, "2" to 3.0f, "3" to 5.0f)
        ),
        onQueryChange = {},
        onProfessorClick = {}
    )
}
