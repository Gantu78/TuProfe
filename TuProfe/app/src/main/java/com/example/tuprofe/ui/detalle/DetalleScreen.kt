package com.example.tuprofe.ui.detalle

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Comment
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tuprofe.R
import com.example.tuprofe.data.Profesor
import com.example.tuprofe.data.ReviewInfo
import com.example.tuprofe.data.local.LocalReview
import com.example.tuprofe.ui.utils.Resena
import com.example.tuprofe.ui.utils.BackgroundImage

@Composable
fun DetalleScreen(
    reviewId: String,
    modifier: Modifier = Modifier,
    detalleViewModel: DetalleViewModel = viewModel(),
    onProfileClick: (Profesor) -> Unit,
    onUserClick: (String) -> Unit
) {
    val uiState by detalleViewModel.uiState.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                detalleViewModel.cargarDetalle(reviewId)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    DetalleContent(
        detalleViewModel = detalleViewModel,
        reviewId = reviewId,
        uiState = uiState,
        onShare = { },
        onComment = { },
        onProfileClick = onProfileClick,
        modifier = modifier,
        onUserClick = onUserClick
    )
}

@Composable
fun DetalleContent(
    detalleViewModel: DetalleViewModel,
    reviewId: String,
    uiState: DetalleState,
    onShare: () -> Unit,
    onComment: () -> Unit,
    onProfileClick: (Profesor) -> Unit,
    modifier: Modifier = Modifier,
    onUserClick: (String) -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        BackgroundImage()

        if (uiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            uiState.selectedReview?.let { review ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
                    contentPadding = PaddingValues(top = 16.dp, bottom = 120.dp)
                ) {
                    item {
                        ReviewCard(
                            detalleViewModel = detalleViewModel,
                            uiState = uiState,
                            reviewId = reviewId,
                            review = review,
                            onComment = onComment,
                            onShare = onShare,
                            onProfileClick = {onProfileClick(review.profesor)},
                            onUserClick = {onUserClick(review.usuario.usuarioId)}
                        )

                        Spacer(modifier = Modifier.height(28.dp))
                    }

                    item {
                        CommentsHeader(
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }

                    items(uiState.respuestas) { respuesta ->
                        CommentCard(
                            respuesta = respuesta,
                        )
                    }
                }
            } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = uiState.errorMessage ?: stringResource(R.string.rese_a_no_encontrada),
                    color = if (uiState.errorMessage != null) Color.Red else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
private fun ReviewCard(
    detalleViewModel: DetalleViewModel,
    uiState: DetalleState,
    reviewId: String,
    review: ReviewInfo,
    onComment: () -> Unit,
    onShare: () -> Unit,
    onProfileClick: () -> Unit,
    onUserClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.5.dp, colorResource(R.color.BordeTuProfe)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column {
            Resena(
                reviewInfo = review,
                onProfileClick = onProfileClick,
                onUserClick = onUserClick
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            ReviewActionBar(
                onLike = {
                    detalleViewModel.sendOrDeleteReviewLike(reviewId, uiState.currentUserId)
                },
                onComment = onComment,
                onShare = onShare,
                isLiked = uiState.selectedReview?.liked?: false
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ReviewCardPreview() {
    ReviewCard(
        detalleViewModel = viewModel(),
        uiState = DetalleState(),
        reviewId = "",
        review = LocalReview.Reviews[0],
        onComment = {},
        onShare = {},
        onUserClick = {},
        onProfileClick = {}
    )
}

@Composable
private fun CommentsHeader(modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(vertical = 12.dp)
    ) {
        Box(
            modifier = Modifier
                .width(4.dp)
                .height(22.dp)
                .background(colorResource(R.color.verdetp))
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = stringResource(R.string.comentarios_m_s_relevantes),
            fontSize = 19.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CommentsHeaderPreview() {
    CommentsHeader()
}


@Composable
private fun CommentCard(
    respuesta: ReviewInfo,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, colorResource(R.color.BordeTuProfe)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Resena(
            reviewInfo = respuesta,
            modifier = Modifier.padding(vertical = 8.dp),
            onProfileClick = { },
            onUserClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CommentCardPreview() {
    CommentCard(
        respuesta = LocalReview.Reviews[1]
    )
}


@Composable
fun ReviewActionBar(
    onLike: () -> Unit,
    onComment: () -> Unit,
    onShare: () -> Unit,
    isLiked: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onLike) {
            if(isLiked) Icon(imageVector = Icons.Filled.ThumbUp, contentDescription = "Like", tint = colorResource(R.color.verdetp))
            else Icon(imageVector = Icons.Outlined.ThumbUp, contentDescription = "Like", tint = colorResource(R.color.verdetp))
        }
        IconButton(onClick = onComment) {
            Icon(imageVector = Icons.AutoMirrored.Outlined.Comment, contentDescription = "Comment", tint = colorResource(R.color.verdetp))
        }
        IconButton(onClick = onShare) {
            Icon(imageVector = Icons.Outlined.Share, contentDescription = "Share", tint = colorResource(R.color.verdetp))
        }
    }
}

@Preview
@Composable
fun ReviewActionBarPreview() {
    ReviewActionBar(onLike = {}, onComment = {}, onShare = {}, isLiked = false)
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DetalleScreenPreview() {
    val mockState = DetalleState(
        selectedReview = LocalReview.Reviews[0],
        respuestas = LocalReview.Reviews.drop(1),
        isLoading = false
    )
    DetalleContent(
        detalleViewModel = viewModel(),
        uiState = mockState,
        reviewId = "",
        onShare = {},
        onComment = {},
        onUserClick = {},
        onProfileClick = {}
    )
}

