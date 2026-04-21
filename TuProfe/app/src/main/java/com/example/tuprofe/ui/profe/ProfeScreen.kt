package com.example.tuprofe.ui.profe

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.tuprofe.R
import com.example.tuprofe.data.Profesor
import com.example.tuprofe.data.local.LocalProfesor
import com.example.tuprofe.data.local.LocalReview
import com.example.tuprofe.ui.main.ResenaCard
import com.example.tuprofe.ui.utils.*

@Composable
fun ProfeScreen(
    modifier: Modifier = Modifier,
    profeViewModel: ProfeViewModel,
    onResenaClick: (String) -> Unit,
    onProfileClick: (Profesor) -> Unit,
    onUserClick: (String) -> Unit
) {
    val uiState by profeViewModel.uiState.collectAsState()
    ProfeContent(
        uiState = uiState,
        onResenaClick = onResenaClick,
        onProfileClick = onProfileClick,
        onUserClick = onUserClick,
        modifier = modifier
    )
}

@Composable
fun ProfeContent(
    uiState: ProfeState,
    onResenaClick: (String) -> Unit,
    onProfileClick: (Profesor) -> Unit,
    onUserClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        BackgroundImage()

        when {
            uiState.isLoading -> {
                // Shimmer placeholder while loading professor + reviews
                Column(modifier = Modifier.fillMaxSize()) {
                    ProfessorInfoCardSkeleton()
                    ReviewListSkeleton(count = 3)
                }
            }

            uiState.profesor != null -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    contentPadding = PaddingValues(bottom = 100.dp)
                ) {
                    item {
                        // Info card slides + fades in first
                        AnimatedScreen(delayMs = 0) {
                            ProfessorInfoCard(
                                professorName = uiState.profesor.nombreProfe,
                                generalRating = uiState.averageRating,
                                professorImageUrl = uiState.profesor.imageprofeUrl,
                                departamento = uiState.profesor.departamento
                            )
                        }
                    }
                    itemsIndexed(
                        uiState.professorReviews,
                        key = { _, r -> r.reviewId }
                    ) { index, review ->
                        AnimatedListItem(index = index) {
                            ResenaCard(
                                reviewInfo = review,
                                onCommentsClick = onResenaClick,
                                onProfileClick = { onProfileClick(review.profesor) },
                                onUserClick = { onUserClick(review.usuario.usuarioId) }
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }

            else -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = stringResource(R.string.rese_a_no_encontrada))
            }
        }
    }
}

// ── Professor info card ───────────────────────────────────────────────────────

@Composable
fun ProfessorInfoCard(
    professorName: String,
    generalRating: Float,
    professorImageUrl: String?,
    departamento: String = "",
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        shape = RoundedCornerShape(28.dp),
        border = BorderStroke(2.dp, colorResource(R.color.BordeTuProfe)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(12.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = professorImageUrl,
                contentDescription = stringResource(R.string.foto_de_perfil),
                placeholder = painterResource(R.drawable.loading_img),
                error = painterResource(R.drawable.avatar),
                modifier = Modifier
                    .size(110.dp)
                    .shadow(6.dp, CircleShape, clip = false)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .border(2.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = professorName, fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
            if (departamento.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = departamento,
                    fontSize = 14.sp,
                    color = colorResource(R.color.verdetp2)
                )
            }
            Spacer(modifier = Modifier.height(18.dp))
            RatingStars(rating = generalRating, starColor = colorResource(R.color.verdetp))
            if (generalRating > 0f) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = String.format("%.1f", generalRating),
                    fontSize = 14.sp,
                    color = colorResource(R.color.verdetp2)
                )
            }
        }
    }
}

// ── Shimmer skeleton for the info card ───────────────────────────────────────

@Composable
fun ProfessorInfoCardSkeleton(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        shape = RoundedCornerShape(28.dp),
        border = BorderStroke(2.dp, colorResource(R.color.BordeTuProfe).copy(alpha = 0.35f)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                Modifier
                    .size(110.dp)
                    .clip(CircleShape)
                    .shimmerEffect()
            )
            Box(
                Modifier
                    .width(160.dp)
                    .height(18.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .shimmerEffect()
            )
            Box(
                Modifier
                    .width(100.dp)
                    .height(12.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .shimmerEffect()
            )
            Box(
                Modifier
                    .width(120.dp)
                    .height(20.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .shimmerEffect()
            )
        }
    }
}

// ── Previews ──────────────────────────────────────────────────────────────────

@Preview(showBackground = true)
@Composable
private fun ProfessorInfoCardPreview() {
    ProfessorInfoCard(
        professorName = "Carlos Parra",
        generalRating = 4F,
        professorImageUrl = null
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ProfeContentPreview() {
    ProfeContent(
        uiState = ProfeState(
            profesor = LocalProfesor.profesores[0],
            professorReviews = LocalReview.Reviews.take(3),
            averageRating = 4F,
            isLoading = false
        ),
        onResenaClick = {},
        onProfileClick = {},
        onUserClick = {}
    )
}
