package com.example.tuprofe.ui.profe

import android.util.Log
import androidx.compose.foundation.BorderStroke
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
import coil.compose.AsyncImage
import com.example.tuprofe.R
import com.example.tuprofe.data.Profesor
import com.example.tuprofe.data.local.LocalProfesor
import com.example.tuprofe.data.local.LocalReview
import com.example.tuprofe.ui.main.ResenaCard
import com.example.tuprofe.ui.utils.BackgroundImage
import com.example.tuprofe.ui.utils.RatingStars


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
        onAddCommentClick = { Log.d("Boton", "Añadir Comentario") },
        onRateClick = { Log.d("Boton", "Calificar") },
        modifier = modifier
    )
}


@Composable
fun ProfeContent(
    uiState: ProfeState,
    onResenaClick: (String) -> Unit,
    onProfileClick: (Profesor) -> Unit,
    onUserClick: (String) -> Unit,
    onAddCommentClick: () -> Unit,
    onRateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        BackgroundImage()

        when {
            uiState.isLoading -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = colorResource(R.color.verdetp))
            }

            uiState.profesor != null -> LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                item {
                    ProfessorInfoCard(
                        professorName = uiState.profesor.nombreProfe,
                        generalRating = uiState.averageRating,
                        professorImageUrl = uiState.profesor.imageprofeUrl
                    )
                }
                items(uiState.professorReviews) { review ->
                    ResenaCard(
                        reviewInfo = review,
                        onCommentsClick = onResenaClick,
                        onProfileClick = { onProfileClick(review.profesor) },
                        onUserClick = { onUserClick(review.usuario.usuarioId) }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            else -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = stringResource(R.string.rese_a_no_encontrada))
            }
        }

        ProfeScreenBottomBar(
            onAddCommentClick = onAddCommentClick,
            onRateClick = onRateClick,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}


@Composable
fun ProfessorInfoCard(
    professorName: String,
    generalRating: Int,
    professorImageUrl: String?,
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
            modifier = Modifier.padding(20.dp).fillMaxWidth(),
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
            Spacer(modifier = Modifier.height(18.dp))
            RatingStars(rating = generalRating, starColor = colorResource(R.color.verdetp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfessorInfoCardPreview() {
    ProfessorInfoCard(
        professorName = "Carlos Parra",
        generalRating = 4,
        professorImageUrl = "https://img.lalr.co/cms/2017/06/16184524/1280x1440_CARLOS-PARRA.jpg?r=6_5&ns=1&w=372&d=2.625"
    )
}


@Composable
private fun ProfeScreenBottomBar(
    onAddCommentClick: () -> Unit,
    onRateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
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
            VerticalDivider(
                modifier = Modifier.height(28.dp),
                color = Color.White.copy(alpha = 0.4f)
            )
            TextButton(onClick = onRateClick, modifier = Modifier.weight(1f)) {
                Text(stringResource(R.string.calificar), color = Color.White)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfeScreenBottomBarPreview() {
    ProfeScreenBottomBar(onAddCommentClick = {}, onRateClick = {})
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ProfeContentPreview() {
    ProfeContent(
        uiState = ProfeState(
            profesor = LocalProfesor.profesores[0],
            professorReviews = LocalReview.Reviews.take(3),
            averageRating = 4,
            isLoading = false
        ),
        onResenaClick = {},
        onProfileClick = {},
        onUserClick = {},
        onAddCommentClick = {},
        onRateClick = {}
    )
}
