package com.example.tuprofe.ui.historia

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
import androidx.compose.material.icons.automirrored.outlined.Comment
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import coil.compose.AsyncImage
import com.example.tuprofe.R
import com.example.tuprofe.data.CommentInfo
import com.example.tuprofe.data.ReviewInfo
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
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 10.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(top = 24.dp, bottom = 80.dp)
            ) {
                item {
                    AnimatedScreen(delayMs = 0) {
                        HistorialHeader(
                            selectedFilter = state.selectedFilter,
                            onFilterClick = { historialViewModel.onFilterClick("") },
                            onFilterSelected = { historialViewModel.setFilter(it) }
                        )
                    }
                }

                val showReviews = state.selectedFilter != HistorialFilter.COMENTARIOS
                val showComments = state.selectedFilter != HistorialFilter.RESENAS

                val isEmpty = (showReviews && state.userReviews.isEmpty() || !showReviews) &&
                        (showComments && state.userComments.isEmpty() || !showComments)

                val noContent = (if (showReviews) state.userReviews else emptyList<ReviewInfo>()).isEmpty() &&
                        (if (showComments) state.userComments else emptyList<CommentInfo>()).isEmpty()

                if (noContent) {
                    item {
                        AnimatedScreen(delayMs = 120) {
                            Text(
                                text = when (state.selectedFilter) {
                                    HistorialFilter.TODO -> "Todavía no has hecho ninguna calificación"
                                    HistorialFilter.RESENAS -> "No tienes reseñas aún"
                                    HistorialFilter.COMENTARIOS -> "No tienes comentarios aún"
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
    onFilterClick: () -> Unit,
    onFilterSelected: (HistorialFilter) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(horizontal = 24.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            HistorialFilter.entries.forEach { filter ->
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
                    modifier = Modifier.weight(1f).pressScaleEffect(),
                    border = BorderStroke(1.5.dp, colorResource(R.color.verdetp)),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = containerColor),
                    shape = RoundedCornerShape(50),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = when (filter) {
                            HistorialFilter.TODO -> "TODO"
                            HistorialFilter.RESENAS -> "RESEÑAS"
                            HistorialFilter.COMENTARIOS -> "COMENTARIOS"
                        },
                        color = textColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.texto_calificaciones),
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            lineHeight = 22.sp,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Spacer(modifier = Modifier.height(12.dp))
        HorizontalDivider(
            thickness = 1.dp,
            color = colorResource(R.color.BordeTuProfe),
            modifier = Modifier.fillMaxWidth()
        )
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
    Card(
        modifier = modifier
            .fillMaxWidth()
            .pressScaleEffect(),
        shape = RoundedCornerShape(28.dp),
        border = BorderStroke(1.dp, colorResource(R.color.BordeTuProfe)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProfesorAvatar(
                imageUrl = review.profesor.imageprofeUrl,
                onClick = { onProfessorClick(review.profesor.profeId) }
            )
            Spacer(modifier = Modifier.width(14.dp))
            HistorialCardBody(
                review = review,
                onVerCalificacionClick = { onVerCalificacionClick(review) },
                onEditClick = onEditClick,
                onDeleteClick = onDeleteClick
            )
        }
    }
}

@Composable
private fun ProfesorAvatar(
    imageUrl: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = imageUrl,
        contentDescription = stringResource(R.string.foto_de_perfil),
        placeholder = painterResource(R.drawable.loading_img),
        error = painterResource(R.drawable.avatar),
        modifier = modifier
            .size(72.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick),
        contentScale = ContentScale.Crop
    )
}

@Composable
private fun HistorialCardBody(
    review: ReviewInfo,
    onVerCalificacionClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = review.profesor.nombreProfe,
                fontWeight = FontWeight.SemiBold,
                fontSize = 17.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = onEditClick,
                modifier = Modifier.pressScaleEffect()
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Editar reseña",
                    tint = colorResource(R.color.verdetp)
                )
            }
            IconButton(
                onClick = onDeleteClick,
                modifier = Modifier.pressScaleEffect()
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar reseña",
                    tint = Color.Red
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RatingStars(rating = review.rating.toFloat(), modifier = Modifier.height(16.dp))
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = review.materia.nombreMateria,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 13.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(8.dp))

        AppButtonRow(
            textoBoton = stringResource(R.string.ver_rese_a),
            onClick = onVerCalificacionClick,
            modifier = Modifier
                .height(34.dp)
                .pressScaleEffect()
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
    Card(
        modifier = modifier
            .fillMaxWidth()
            .pressScaleEffect(),
        shape = RoundedCornerShape(28.dp),
        border = BorderStroke(1.dp, colorResource(R.color.BordeTuProfe)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(
                        color = colorResource(R.color.verdetp).copy(alpha = 0.1f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.Comment,
                    contentDescription = null,
                    tint = colorResource(R.color.verdetp),
                    modifier = Modifier.size(26.dp)
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            CommentHistorialCardBody(
                comment = comment,
                onVerComentarioClick = onVerComentarioClick,
                onEditClick = onEditClick,
                onDeleteClick = onDeleteClick
            )
        }
    }
}

@Composable
private fun CommentHistorialCardBody(
    comment: CommentInfo,
    onVerComentarioClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Comentario",
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                color = colorResource(R.color.verdetp),
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = onEditClick,
                modifier = Modifier.pressScaleEffect()
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Editar comentario",
                    tint = colorResource(R.color.verdetp)
                )
            }
            IconButton(
                onClick = onDeleteClick,
                modifier = Modifier.pressScaleEffect()
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar comentario",
                    tint = Color.Red
                )
            }
        }

        Text(
            text = comment.content,
            fontSize = 14.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = comment.time,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        AppButtonRow(
            textoBoton = "VER COMENTARIO",
            onClick = onVerComentarioClick,
            modifier = Modifier
                .height(34.dp)
                .pressScaleEffect()
        )
    }
}
