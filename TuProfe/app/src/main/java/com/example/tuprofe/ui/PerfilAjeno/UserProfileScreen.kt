package com.example.tuprofe.ui.PerfilAjeno

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tuprofe.ui.utils.BackgroundImage
import com.example.tuprofe.ui.utils.ProfileHeaderCard
import com.example.tuprofe.ui.utils.Resena

@Composable
fun UserProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: UserProfileViewModel = hiltViewModel(),
    onProfessorClick: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(modifier = modifier.fillMaxSize()) {
        BackgroundImage()

        when {
            uiState.isLoading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            uiState.errorMessage != null -> {
                Text(
                    text = uiState.errorMessage ?: "Error",
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.error
                )
            }
            uiState.user != null -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        ProfileHeaderCard(
                            username = uiState.user!!.nombreUsu,
                            email = uiState.user!!.email,
                            carrera = uiState.user!!.carrera,
                            imageUrl = uiState.user!!.imageprofeUrl,
                            onProfileClick = {}, // No action on header for other profile
                            showStar = true
                        )
                    }

                    items(uiState.userReviews) { review ->
                        Resena(
                            reviewInfo = review,
                            onProfileClick = { onProfessorClick(review.profesor.profeId) },
                            onUserClick = {}, // No action on user click
                        )
                    }
                }
            }
        }
    }
}
