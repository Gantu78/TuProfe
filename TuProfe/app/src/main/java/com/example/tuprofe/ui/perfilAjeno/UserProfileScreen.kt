package com.example.tuprofe.ui.perfilAjeno

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.tuprofe.R
import com.example.tuprofe.data.ReviewInfo
import com.example.tuprofe.data.Usuario
import com.example.tuprofe.data.local.LocalReview
import com.example.tuprofe.ui.theme.BebasNeue
import com.example.tuprofe.ui.theme.TuProfeTheme
import com.example.tuprofe.ui.utils.*

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
        onFollowClick = { viewModel.followOrUnfollowUser(uiState.user.usuarioId) },
        onFollowersClick = { viewModel.openFollowersSheet() },
        onFollowingClick = { viewModel.openFollowingSheet() },
        onDismissSheet = { viewModel.closeSheet() },
        onFollowInList = { viewModel.followOrUnfollowInList(it) },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileContent(
    state: UserProfileState,
    onProfessorClick: (String) -> Unit,
    onFollowClick: () -> Unit = {},
    onFollowersClick: () -> Unit = {},
    onFollowingClick: () -> Unit = {},
    onDismissSheet: () -> Unit = {},
    onFollowInList: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        BackgroundImage()
        when {
            state.isLoading -> UserProfileLoadingState()
            state.errorMessage != null -> UserProfileErrorState(state.errorMessage)
            else -> UserProfileLoaded(
                user = state.user,
                reviews = state.userReviews,
                isOwnProfile = state.currentUserId == state.user.usuarioId,
                onProfessorClick = onProfessorClick,
                onFollowClick = onFollowClick,
                onFollowersClick = onFollowersClick,
                onFollowingClick = onFollowingClick
            )
        }
    }

    val showSheet = state.showFollowersSheet || state.showFollowingSheet
    if (showSheet) {
        val title = if (state.showFollowersSheet) "Seguidores" else "Siguiendo"
        val list = if (state.showFollowersSheet) state.followersList else state.followingList

        ModalBottomSheet(onDismissRequest = onDismissSheet) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 4.dp)
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            if (state.isLoadingList) {
                Box(
                    modifier = Modifier.fillMaxWidth().height(120.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = colorResource(R.color.verdetp))
                }
            } else if (list.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxWidth().height(120.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay usuarios aún",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.navigationBarsPadding()
                ) {
                    itemsIndexed(list, key = { _, u -> u.usuarioId }) { index, usuario ->
                        AnimatedListItem(index = index) {
                            UserListItem(
                                usuario = usuario,
                                isCurrentUser = usuario.usuarioId == state.currentUserId,
                                onFollowClick = { onFollowInList(usuario.usuarioId) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun UserProfileLoaded(
    user: Usuario,
    reviews: List<ReviewInfo>,
    isOwnProfile: Boolean,
    onProfessorClick: (String) -> Unit,
    onFollowClick: () -> Unit,
    onFollowersClick: () -> Unit = {},
    onFollowingClick: () -> Unit = {}
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        item {
            AnimatedScreen(delayMs = 0) {
                UserProfileHeader(
                    user = user,
                    isOwnProfile = isOwnProfile,
                    onFollowClick = onFollowClick,
                    onFollowersClick = onFollowersClick,
                    onFollowingClick = onFollowingClick
                )
            }
        }

        item { Spacer(Modifier.height(16.dp)) }

        item {
            AnimatedScreen(delayMs = 80) {
                ReviewsSectionHeader(
                    reviewCount = reviews.size,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }
        }

        item { Spacer(Modifier.height(8.dp)) }

        if (reviews.isEmpty()) {
            item {
                AnimatedScreen(delayMs = 120) { EmptyReviewsMessage() }
            }
        } else {
            itemsIndexed(reviews, key = { _, r -> r.reviewId }) { index, review ->
                AnimatedListItem(index = index) {
                    ReviewCard(
                        review = review,
                        onProfessorClick = onProfessorClick,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                    Spacer(Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
private fun UserProfileHeader(
    user: Usuario,
    isOwnProfile: Boolean,
    onFollowClick: () -> Unit,
    onFollowersClick: () -> Unit = {},
    onFollowingClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(top = 28.dp, bottom = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = user.imageprofeUrl,
            contentDescription = null,
            placeholder = painterResource(R.drawable.loading_img),
            error = painterResource(R.drawable.avatar),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(96.dp)
                .shadow(6.dp, CircleShape, clip = false)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        )

        Spacer(Modifier.height(12.dp))

        Text(
            text = user.nombreUsu,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = user.carrera,
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 2.dp)
        )

        Spacer(Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(40.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            StatItem(label = "Seguidores", count = user.followersCount, onClick = onFollowersClick)
            StatItem(label = "Siguiendo", count = user.followingCount, onClick = onFollowingClick)
        }

        if (!isOwnProfile) {
            Spacer(Modifier.height(16.dp))

            // Animate follow button container color
            val containerColor by animateColorAsState(
                targetValue = if (user.followed) Color.Transparent else colorResource(R.color.verdetp),
                animationSpec = tween(300),
                label = "followBg"
            )
            val contentColor by animateColorAsState(
                targetValue = if (user.followed) colorResource(R.color.verdetp) else Color.White,
                animationSpec = tween(300),
                label = "followContent"
            )

            Button(
                onClick = onFollowClick,
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = containerColor,
                    contentColor = contentColor
                ),
                border = if (user.followed) BorderStroke(1.5.dp, colorResource(R.color.verdetp)) else null,
                modifier = Modifier
                    .width(140.dp)
                    .height(40.dp)
                    .pressScaleEffect()
            ) {
                Text(
                    text = if (user.followed) "Siguiendo" else "Seguir",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp
                )
            }
        }

        Spacer(Modifier.height(20.dp))
        HorizontalDivider(color = colorResource(R.color.BordeTuProfe))
    }
}

@Composable
private fun StatItem(label: String, count: Int, onClick: () -> Unit = {}) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .pressScaleEffect()
    ) {
        Text(
            text = "$count",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun UserListItem(
    usuario: Usuario,
    isCurrentUser: Boolean,
    onFollowClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = usuario.imageprofeUrl,
            contentDescription = null,
            placeholder = painterResource(R.drawable.loading_img),
            error = painterResource(R.drawable.avatar),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        )
        Spacer(Modifier.width(12.dp))
        Text(
            text = usuario.nombreUsu,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
        if (!isCurrentUser) {
            val containerColor by animateColorAsState(
                targetValue = if (usuario.followed) colorResource(R.color.verdetp) else Color.Transparent,
                animationSpec = tween(250),
                label = "listFollowBg"
            )
            val contentColor by animateColorAsState(
                targetValue = if (usuario.followed) Color.White else colorResource(R.color.verdetp),
                animationSpec = tween(250),
                label = "listFollowContent"
            )
            OutlinedButton(
                onClick = onFollowClick,
                border = BorderStroke(1.5.dp, colorResource(R.color.verdetp)),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 4.dp),
                modifier = Modifier
                    .height(34.dp)
                    .pressScaleEffect(),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = containerColor,
                    contentColor = contentColor
                )
            ) {
                Text(
                    text = if (usuario.followed) "Siguiendo" else "Seguir",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

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

@Composable
private fun ReviewCard(
    review: ReviewInfo,
    onProfessorClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .pressScaleEffect(),
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
    imageprofeUrl = null,
    followersCount = 128,
    followingCount = 47,
    followed = false
)

@Preview(showBackground = true, showSystemUi = true, name = "Con reseñas – Claro")
@Composable
private fun UserProfileWithReviewsLightPreview() {
    TuProfeTheme(darkTheme = false) {
        UserProfileContent(
            state = UserProfileState(user = previewUser, userReviews = LocalReview.Reviews.take(3)),
            onProfessorClick = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Con reseñas – Oscuro", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun UserProfileWithReviewsDarkPreview() {
    TuProfeTheme(darkTheme = true) {
        UserProfileContent(
            state = UserProfileState(user = previewUser, userReviews = LocalReview.Reviews.take(3)),
            onProfessorClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Siguiendo")
@Composable
private fun UserProfileFollowedPreview() {
    TuProfeTheme {
        UserProfileContent(
            state = UserProfileState(user = previewUser.copy(followed = true, followersCount = 129)),
            onProfessorClick = {}
        )
    }
}
