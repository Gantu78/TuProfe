package com.example.tuprofe.ui.historia

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.tuprofe.R
import com.example.tuprofe.data.CommentInfo
import com.example.tuprofe.data.ReviewInfo
import com.example.tuprofe.ui.comment.detalle.ComentarioContent
import com.example.tuprofe.ui.main.ResenaCard
import com.example.tuprofe.ui.utils.*

@Composable
fun HistorialScreen(
    historialViewModel: HistorialViewModel,
    onProfessorClick: (String) -> Unit,
    onVerCalificacionClick: (ReviewInfo) -> Unit,
    onEditClick: (String) -> Unit,
    onVerComentarioClick: (String) -> Unit,
    onEditCommentClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val state by historialViewModel.uiState.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) historialViewModel.cargarHistorial()
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    Box(modifier = modifier.fillMaxSize()) {
        BackgroundImage()

        if (state.isLoading && state.userReviews.isEmpty() && state.userComments.isEmpty()) {
            ReviewListSkeleton(count = 4)
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(0.dp),
                contentPadding = PaddingValues(top = 0.dp, bottom = 100.dp)
            ) {
                item {
                    AnimatedScreen(delayMs = 0) {
                        HistorialHeader(
                            selectedFilter = state.selectedFilter,
                            onFilterSelected = { historialViewModel.setFilter(it) }
                        )
                    }
                }

                val showReviews = state.selectedFilter != HistorialFilter.COMENTARIOS
                val showComments = state.selectedFilter != HistorialFilter.RESENAS

                val noContent = (if (showReviews) state.userReviews else emptyList<ReviewInfo>()).isEmpty() &&
                        (if (showComments) state.userComments else emptyList<CommentInfo>()).isEmpty()

                if (noContent) {
                    item {
                        AnimatedScreen(delayMs = 120) {
                            Text(
                                text = when (state.selectedFilter) {
                                    HistorialFilter.TODO -> stringResource(R.string.historial_vacio)
                                    HistorialFilter.RESENAS -> stringResource(R.string.sin_resenas_historial)
                                    HistorialFilter.COMENTARIOS -> stringResource(R.string.sin_comentarios_historial)
                                },
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 40.dp)
                            )
                        }
                    }
                } else {
                    if (showReviews) {
                        itemsIndexed(
                            state.userReviews,
                            key = { _, review -> "review_${review.reviewId}" }
                        ) { index, review ->
                            AnimatedListItem(index = index) {
                                HistorialCard(
                                    review = review,
                                    onVerCalificacionClick = onVerCalificacionClick,
                                    onProfessorClick = onProfessorClick,
                                    onEditClick = { onEditClick(review.reviewId) },
                                    onDeleteClick = { historialViewModel.deleteReview(review.reviewId) }
                                )
                            }
                        }
                    }

                    if (showComments) {
                        itemsIndexed(
                            state.userComments,
                            key = { _, comment -> "comment_${comment.commentId}" }
                        ) { index, comment ->
                            AnimatedListItem(index = if (showReviews) state.userReviews.size + index else index) {
                                CommentHistorialCard(
                                    comment = comment,
                                    onVerComentarioClick = { onVerComentarioClick(comment.commentId) },
                                    onEditClick = { onEditCommentClick(comment.commentId) },
                                    onDeleteClick = { historialViewModel.deleteComment(comment.commentId) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HistorialHeader(
    selectedFilter: HistorialFilter,
    onFilterSelected: (HistorialFilter) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.mi_historial),
            fontSize = 36.sp,
            fontWeight = FontWeight.Black,
            color = colorResource(R.color.verdetp),
            modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 12.dp)
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(start = 24.dp, end = 24.dp, bottom = 8.dp)
        ) {
            items(HistorialFilter.entries) { filter ->
                val isSelected = selectedFilter == filter
                val containerColor by animateColorAsState(
                    targetValue = if (isSelected) colorResource(R.color.verdetp) else Color.Transparent,
                    animationSpec = tween(200),
                    label = "filterColor"
                )
                val textColor by animateColorAsState(
                    targetValue = if (isSelected) Color.White else colorResource(R.color.verdetp),
                    animationSpec = tween(200),
                    label = "filterTextColor"
                )
                OutlinedButton(
                    onClick = { onFilterSelected(filter) },
                    border = BorderStroke(1.5.dp, colorResource(R.color.verdetp)),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = containerColor),
                    shape = RoundedCornerShape(50),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = when (filter) {
                            HistorialFilter.TODO -> stringResource(R.string.filtro_todo)
                            HistorialFilter.RESENAS -> stringResource(R.string.filtro_resenas)
                            HistorialFilter.COMENTARIOS -> stringResource(R.string.filtro_comentarios)
                        },
                        color = textColor,
                        fontSize = 14.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Composable
fun HistorialCard(
    review: ReviewInfo,
    onProfessorClick: (String) -> Unit,
    onVerCalificacionClick: (ReviewInfo) -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        ResenaCard(
            reviewInfo = review,
            onCommentsClick = { _ -> onVerCalificacionClick(review) },
            onProfileClick = { onProfessorClick(review.profesor.profeId) },
            onUserClick = {}
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                onClick = onEditClick,
                colors = ButtonDefaults.textButtonColors(contentColor = colorResource(R.color.verdetp))
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = stringResource(R.string.editar),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            TextButton(
                onClick = { showDeleteDialog = true },
                colors = ButtonDefaults.textButtonColors(contentColor = Color.Red)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = stringResource(R.string.eliminar),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(stringResource(R.string.eliminar_resena_confirmacion)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        onDeleteClick()
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text(stringResource(R.string.eliminar))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text(stringResource(R.string.cancelar))
                }
            }
        )
    }
}

@Composable
fun CommentHistorialCard(
    comment: CommentInfo,
    onVerComentarioClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(top = 4.dp)
                .pressScaleEffect()
                .clickable(onClick = onVerComentarioClick),
            shape = RoundedCornerShape(18.dp),
            border = BorderStroke(1.dp, colorResource(R.color.BordeTuProfe)),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            ComentarioContent(
                comment = comment,
                onUserClick = {},
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                onClick = onEditClick,
                colors = ButtonDefaults.textButtonColors(contentColor = colorResource(R.color.verdetp))
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = stringResource(R.string.editar),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            TextButton(
                onClick = { showDeleteDialog = true },
                colors = ButtonDefaults.textButtonColors(contentColor = Color.Red)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = stringResource(R.string.eliminar),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(stringResource(R.string.eliminar_comentario_confirmacion)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        onDeleteClick()
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text(stringResource(R.string.eliminar))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text(stringResource(R.string.cancelar))
                }
            }
        )
    }
}