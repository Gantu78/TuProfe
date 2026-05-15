package com.example.tuprofe.ui.profe

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
        onGenerarResumenClick = { profeViewModel.generarResumenIA() },
        modifier = modifier
    )
}

@Composable
fun ProfeContent(
    uiState: ProfeState,
    onResenaClick: (String) -> Unit,
    onProfileClick: (Profesor) -> Unit,
    onUserClick: (String) -> Unit,
    onGenerarResumenClick: () -> Unit,
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

                    // IA Summary Section
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if (uiState.isLoadingIA) {
                                CircularProgressIndicator(
                                    color = colorResource(R.color.verdetp),
                                    modifier = Modifier.padding(16.dp)
                                )
                                Text(
                                    text = "Analizando reseñas...",
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )
                            } else if (uiState.resumenIA == null) {
                                AppButton(
                                    textoBoton = "Resumen",
                                    onClick = onGenerarResumenClick,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }

                            uiState.errorIA?.let {
                                Text(
                                    text = it,
                                    color = Color.Red,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            }
                        }
                    }

                    item {
                        AnimatedVisibility(
                            visible = uiState.resumenIA != null,
                            enter = fadeIn() + expandVertically()
                        ) {
                            uiState.resumenIA?.let { resumen ->
                                ResumenIACard(resumen = resumen)
                            }
                        }
                    }

                    item {
                        Text(
                            text = "Reseñas de alumnos",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 24.dp, top = 16.dp, bottom = 8.dp),
                            color = colorResource(R.color.verdetp)
                        )
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

@Composable
fun ResumenIACard(resumen: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(8.dp),
        border = BorderStroke(
            width = 2.dp,
            brush = Brush.linearGradient(
                colors = listOf(
                    colorResource(R.color.verdetp),
                    colorResource(R.color.verdetp2)
                )
            )
        )
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = colorResource(R.color.verdetp),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Resumen de Inteligencia Artificial",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = colorResource(R.color.verdetp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = resumen,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                color = Color.DarkGray
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Generado por Llama 3.1 70B en Groq",
                fontSize = 10.sp,
                color = Color.LightGray,
                modifier = Modifier.align(Alignment.End)
            )
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
            isLoading = false,
            resumenIA = "Este es un resumen de prueba generado por la IA."
        ),
        onResenaClick = {},
        onProfileClick = {},
        onUserClick = {},
        onGenerarResumenClick = {}
    )
}
