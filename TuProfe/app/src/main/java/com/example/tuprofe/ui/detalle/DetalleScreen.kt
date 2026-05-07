package com.example.tuprofe.ui.detalle

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tuprofe.R
import com.example.tuprofe.data.CommentInfo
import com.example.tuprofe.data.Profesor
import com.example.tuprofe.data.ReviewInfo
import com.example.tuprofe.ui.comment.detalle.ComentarioContent
import com.example.tuprofe.ui.comment.detalle.CommentComposeSheet
import com.example.tuprofe.ui.utils.BackgroundImage
import com.example.tuprofe.ui.utils.Resena
import com.example.tuprofe.ui.utils.pressScaleEffect

@Composable
fun DetalleScreen(
    reviewId: String,
    modifier: Modifier = Modifier,
    detalleViewModel: DetalleViewModel = viewModel(),
    onProfileClick: (Profesor) -> Unit,
    onUserClick: (String) -> Unit,
    onCommentClick: (String) -> Unit
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

    if (uiState.showCommentSheet) {
        CommentComposeSheet(
            contextText = uiState.selectedReview?.content ?: "",
            contextUser = uiState.selectedReview?.usuario?.nombreUsu ?: "",
            text = uiState.commentText,
            onTextChange = { detalleViewModel.onCommentTextChange(it) },
            onDismiss = { detalleViewModel.closeCommentSheet() },
            onSubmit = { detalleViewModel.submitComment(reviewId) },
            isSubmitting = uiState.isSubmittingComment,
            submitLabel = "COMENTAR"
        )
    }

    DetalleContent(
        detalleViewModel = detalleViewModel,
        reviewId = reviewId,
        uiState = uiState,
        onShare = {},
        onComment = { detalleViewModel.openCommentSheet() },
        onProfileClick = onProfileClick,
        onUserClick = onUserClick,
        onCommentClick = onCommentClick,
        modifier = modifier
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
    onUserClick: (String) -> Unit,
    onCommentClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize().testTag("detalleScreen")) {
        BackgroundImage()

        if (uiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            AnimatedVisibility(
                visible = uiState.selectedReview != null,
                enter = fadeIn(tween(320)) +
                        slideInVertically(
                            animationSpec = tween(380, easing = FastOutSlowInEasing),
                            initialOffsetY = { it / 6 }
                        )
            ) {
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
                                onProfileClick = { onProfileClick(review.profesor) },
                                onUserClick = { onUserClick(review.usuario.usuarioId) }
                            )
                            Spacer(modifier = Modifier.height(28.dp))
                        }

                        item {
                            CommentsHeader(modifier = Modifier.padding(bottom = 16.dp))
                        }

                        if (uiState.isLoadingComments) {
                            item {
                                Box(
                                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(28.dp),
                                        color = colorResource(R.color.verdetp)
                                    )
                                }
                            }
                        } else {
                            items(uiState.comments, key = { it.commentId }) { comment ->
                                CommentCard(
                                    comment = comment,
                                    onUserClick = { onUserClick(comment.usuario.usuarioId) },
                                    onClick = { onCommentClick(comment.commentId) }
                                )
                            }
                        }
                    }
                }
            }

            if (uiState.selectedReview == null && uiState.errorMessage != null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = uiState.errorMessage, color = Color.Red)
                }
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
                isLiked = uiState.selectedReview?.liked ?: false
            )
        }
    }
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

@Composable
private fun CommentCard(
    comment: CommentInfo,
    onUserClick: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .pressScaleEffect()
            .clickable(onClick = onClick)
            .testTag("comment_card"),
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, colorResource(R.color.BordeTuProfe)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        ComentarioContent(
            comment = comment,
            onUserClick = onUserClick,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        )
    }
}

@Composable
fun ReviewActionBar(
    onLike: () -> Unit,
    onComment: () -> Unit,
    onShare: () -> Unit,
    isLiked: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        var likeTriggered by remember { mutableStateOf(false) }
        val likeScale by animateFloatAsState(
            targetValue = if (likeTriggered) 1.35f else 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioHighBouncy,
                stiffness = Spring.StiffnessMedium
            ),
            finishedListener = { likeTriggered = false },
            label = "likeScale"
        )

        IconButton(
            onClick = {
                likeTriggered = true
                onLike()
            }
        ) {
            Icon(
                imageVector = if (isLiked) Icons.Filled.ThumbUp else Icons.Outlined.ThumbUp,
                contentDescription = "Like",
                tint = colorResource(R.color.verdetp),
                modifier = Modifier.graphicsLayer {
                    scaleX = likeScale
                    scaleY = likeScale
                }
            )
        }

        IconButton(onClick = onComment) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.Comment,
                contentDescription = "Comment",
                tint = colorResource(R.color.verdetp)
            )
        }

        IconButton(onClick = onShare) {
            Icon(
                imageVector = Icons.Outlined.Share,
                contentDescription = "Share",
                tint = colorResource(R.color.verdetp)
            )
        }
    }
}
