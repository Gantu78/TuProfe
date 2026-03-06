package com.example.tuprofe.ui.Profe

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tuprofe.R
import com.example.tuprofe.data.local.LocalProfesor
import com.example.tuprofe.data.local.LocalReview
import com.example.tuprofe.ui.Main.ResenaCard
import com.example.tuprofe.ui.utils.BackgroundImage
import com.example.tuprofe.ui.utils.RatingStars

@Composable
fun ProfeScreen(
    modifier: Modifier = Modifier,
    profeViewModel: ProfeViewModel = viewModel()
) {
    val uiState by profeViewModel.uiState.collectAsState()

    ProfeContent(
        uiState = uiState,
        onReviewClick = { profeViewModel.onReviewClick(it) },
        onProfileClick = { profeViewModel.onProfileClick(it) },
        modifier = modifier
    )
}

@Composable
fun ProfeContent(
    uiState: ProfeState,
    onReviewClick: (Int) -> Unit,
    onProfileClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        BackgroundImage()

        if (uiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            uiState.profesor?.let { profesor ->
                Column(Modifier.fillMaxSize()) {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        contentPadding = PaddingValues(bottom = 80.dp)
                    ) {
                        item {
                            ProfessorInfoCard(
                                professorName = profesor.nombreProfe,
                                professorSubjects = profesor.materia,
                                generalRating = uiState.averageRating,
                                professorImageRes = profesor.imageprofeId
                            )
                        }

                        items(uiState.professorReviews) { review ->
                            ResenaCard(
                                reviewInfo = review,
                                onCommentsClick = { onReviewClick(review.reviewId) },
                                onProfileClick = { onProfileClick(review.profesor.profeId) }
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Profesor no encontrado")
            }
        }

        ProfeScreenBottomBar(
            onAddCommentClick = { Log.d("Boton", "Añadir Comentario") },
            onRateClick = { Log.d("Boton", "Calificar") },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun ProfessorInfoCard(
    professorName: String,
    professorSubjects: String,
    generalRating: Int,
    @DrawableRes professorImageRes: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
        shape = RoundedCornerShape(28.dp),
        border = BorderStroke(2.dp, colorResource(R.color.BordeTuProfe)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(professorImageRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(110.dp)
                    .shadow(6.dp, CircleShape, clip = false)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .border(2.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), CircleShape)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = professorName, fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
            Text(text = professorSubjects, fontSize = 15.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(18.dp))
            RatingStars(rating = generalRating, starColor = colorResource(R.color.verdetp))
        }
    }
}

@Composable
private fun ProfeScreenBottomBar(
    onAddCommentClick: () -> Unit,
    onRateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp),
        shape = RoundedCornerShape(50.dp),
        colors = CardDefaults.cardColors(containerColor = colorResource(R.color.verdetp))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().height(54.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onAddCommentClick, modifier = Modifier.weight(1f)) {
                Text(stringResource(R.string.a_adir_comentario), color = Color.White)
            }
            VerticalDivider(modifier = Modifier.height(28.dp), color = Color.White.copy(alpha = 0.4f))
            TextButton(onClick = onRateClick, modifier = Modifier.weight(1f)) {
                Text(stringResource(R.string.calificar), color = Color.White)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfeScreenPreview() {
    val mockState = ProfeState(
        profesor = LocalProfesor.profesores[0],
        professorReviews = LocalReview.Reviews.filter { it.profesor.profeId == 1 },
        averageRating = 4,
        isLoading = false
    )
    ProfeContent(
        uiState = mockState,
        onReviewClick = {},
        onProfileClick = {}
    )
}
