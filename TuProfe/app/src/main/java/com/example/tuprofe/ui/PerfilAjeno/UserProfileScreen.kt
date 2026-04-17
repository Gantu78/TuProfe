package com.example.tuprofe.ui.PerfilAjeno

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tuprofe.R
import com.example.tuprofe.data.ReviewInfo
import com.example.tuprofe.data.Usuario
import com.example.tuprofe.data.local.LocalReview
import com.example.tuprofe.ui.theme.BebasNeue
import com.example.tuprofe.ui.theme.TuProfeTheme
import com.example.tuprofe.ui.utils.BackgroundImage
import com.example.tuprofe.ui.utils.ProfileHeaderCard
import com.example.tuprofe.ui.utils.Resena

// ── Entry point (stateful) ────────────────────────────────────────────────────

@Composable
fun UserProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: UserProfileViewModel = hiltViewModel(),
    onProfessorClick: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    UserProfileContent(
        state = uiState,
        onProfessorClick = onProfessorClick,
        modifier = modifier
    )
}

// ── Stateless root (state hoisted) ───────────────────────────────────────────

@Composable
fun UserProfileContent(
    state: UserProfileState,
    onProfessorClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        BackgroundImage()
        when {
            state.isLoading -> UserProfileLoadingState()
            state.errorMessage != null -> UserProfileErrorState(state.errorMessage)
            state.user != null -> UserProfileLoaded(
                user = state.user,
                reviews = state.userReviews,
                onProfessorClick = onProfessorClick
            )
        }
    }
}

// ── Loaded layout ─────────────────────────────────────────────────────────────

@Composable
private fun UserProfileLoaded(
    user: Usuario,
    reviews: List<ReviewInfo>,
    onProfessorClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 30.dp),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        item {
            ProfileHeaderCard(
                username = user.nombreUsu,
                email = user.email,
                carrera = user.carrera,
                imageUrl = user.imageprofeUrl,
                onProfileClick = {},
                showStar = true
            )
        }

        item { Spacer(Modifier.height(16.dp)) }

        item {
            ReviewsSectionHeader(reviewCount = reviews.size)
        }

        item { Spacer(Modifier.height(8.dp)) }

        if (reviews.isEmpty()) {
            item { EmptyReviewsMessage() }
        } else {
            items(reviews, key = { it.reviewId }) { review ->
                ReviewCard(
                    review = review,
                    onProfessorClick = onProfessorClick
                )
                Spacer(Modifier.height(12.dp))
            }
        }
    }
}
// ── Section header ────────────────────────────────────────────────────────────

@Composable
private fun ReviewsSectionHeader(reviewCount: Int, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Reseñas",
                fontFamily = BebasNeue,
                fontSize = 26.sp,
                color = colorResource(R.color.verdetp)
            )
            if (reviewCount > 0) {
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = colorResource(R.color.verdetp).copy(alpha = 0.12f)
                ) {
                    Text(
                        text = "$reviewCount",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(R.color.verdetp)
                    )
                }
            }
        }
        HorizontalDivider(
            modifier = Modifier.padding(top = 4.dp),
            color = colorResource(R.color.BordeTuProfe)
        )
    }
}

// ── Review card (wraps Resena to provide surface background) ─────────────────

@Composable
private fun ReviewCard(
    review: ReviewInfo,
    onProfessorClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        border = BorderStroke(2.dp, colorResource(R.color.BordeTuProfe)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Resena(
            reviewInfo = review,
            onProfileClick = { onProfessorClick(review.profesor.profeId) },
            onUserClick = {}
        )
    }
}

// ── Empty & status states ─────────────────────────────────────────────────────

@Composable
private fun EmptyReviewsMessage(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = Icons.Outlined.Star,
            contentDescription = null,
            modifier = Modifier.size(46.dp),
            tint = colorResource(R.color.verdetp).copy(alpha = 0.35f)
        )
        Text(
            text = "Sin reseñas aún",
            fontFamily = BebasNeue,
            fontSize = 22.sp,
            color = colorResource(R.color.verdetp).copy(alpha = 0.55f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun UserProfileLoadingState(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = colorResource(R.color.verdetp))
    }
}

@Composable
private fun UserProfileErrorState(message: String, modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(24.dp)
        )
    }
}

// ── Previews ──────────────────────────────────────────────────────────────────

private val previewUser = Usuario(
    usuarioId = "1",
    nombreUsu = "Gantu970",
    email = "g.samjg@javeriana.edu.co",
    carrera = "Ingeniería de Sistemas",
    imageprofeUrl = null
)

@Preview(showBackground = true, showSystemUi = true, name = "Con reseñas – Claro")
@Composable
private fun UserProfileWithReviewsLightPreview() {
    TuProfeTheme(darkTheme = false) {
        UserProfileContent(
            state = UserProfileState(
                user = previewUser,
                userReviews = LocalReview.Reviews.take(3)
            ),
            onProfessorClick = {}
        )
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    name = "Con reseñas – Oscuro",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun UserProfileWithReviewsDarkPreview() {
    TuProfeTheme(darkTheme = true) {
        UserProfileContent(
            state = UserProfileState(
                user = previewUser,
                userReviews = LocalReview.Reviews.take(3)
            ),
            onProfessorClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Sin reseñas")
@Composable
private fun UserProfileEmptyPreview() {
    TuProfeTheme {
        UserProfileContent(
            state = UserProfileState(user = previewUser, userReviews = emptyList()),
            onProfessorClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Cargando")
@Composable
private fun UserProfileLoadingPreview() {
    TuProfeTheme {
        UserProfileContent(state = UserProfileState(isLoading = true), onProfessorClick = {})
    }
}

@Preview(showBackground = true, name = "Error")
@Composable
private fun UserProfileErrorPreview() {
    TuProfeTheme {
        UserProfileContent(
            state = UserProfileState(errorMessage = "No se pudo cargar el perfil"),
            onProfessorClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Encabezado de sección")
@Composable
private fun ReviewsSectionHeaderPreview() {
    TuProfeTheme {
        Column(modifier = Modifier.padding(30.dp)) {
            ReviewsSectionHeader(reviewCount = 5)
        }
    }
}