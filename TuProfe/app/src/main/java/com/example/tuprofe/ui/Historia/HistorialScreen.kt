package com.example.tuprofe.ui.Historia

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tuprofe.data.ReviewInfo
import com.example.tuprofe.ui.utils.BackgroundImage
import com.example.tuprofe.ui.utils.Resena

@Composable
fun HistorialScreen(
    modifier: Modifier = Modifier,
    historialViewModel: HistorialViewModel = viewModel(),
    onFilterClick: () -> Unit,
    onVerCalificacionClick: (ReviewInfo) -> Unit
) {
    val uiState by historialViewModel.uiState.collectAsState()

    Box(modifier = modifier.fillMaxSize()) {
        BackgroundImage()

        if (uiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(uiState.userReviews) { review ->
                    Resena(
                        reviewInfo = review,
                        onProfileClick = { /* Acción al hacer click en el perfil */ },
                        onCommentsClick = { onVerCalificacionClick(review) }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HistorialScreenPreview() {
    HistorialScreen(
        onFilterClick = { },
        onVerCalificacionClick = { }
    )
}
