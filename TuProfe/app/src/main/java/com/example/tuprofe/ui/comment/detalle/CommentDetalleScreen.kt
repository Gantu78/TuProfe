package com.example.tuprofe.ui.comment.detalle

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import coil.compose.AsyncImage
import com.example.tuprofe.R
import com.example.tuprofe.data.CommentInfo
import com.example.tuprofe.ui.utils.BackgroundImage
import com.example.tuprofe.ui.utils.pressScaleEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentDetalleScreen(
    commentId: String,
    viewModel: CommentDetalleViewModel,
    onCommentClick: (String) -> Unit,
    onUserClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.cargarComentario(commentId)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    if (uiState.showReplySheet) {
        CommentComposeSheet(
            contextText = uiState.selectedComment?.content ?: "",
            contextUser = uiState.selectedComment?.usuario?.nombreUsu ?: "",
            text = uiState.replyText,
            onTextChange = { viewModel.onReplyTextChange(it) },
            onDismiss = { viewModel.closeReplySheet() },
            onSubmit = { viewModel.submitReply(commentId) },
            isSubmitting = uiState.isSubmittingReply,
            submitLabel = "RESPONDER"
        )
    }

    Box(modifier = modifier.fillMaxSize().testTag("commentDetalleScreen")) {
        BackgroundImage()

        if (uiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            AnimatedVisibility(
                visible = uiState.selectedComment != null,
                enter = fadeIn(tween(320)) + slideInVertically(
                    animationSpec = tween(380, easing = FastOutSlowInEasing),
                    initialOffsetY = { it / 6 }
                )
            ) {
                uiState.selectedComment?.let { comment ->
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp),
                        contentPadding = PaddingValues(top = 16.dp, bottom = 120.dp)
                    ) {
                        item {
                            CommentMainCard(
                                comment = comment,
                                onLike = { viewModel.sendOrDeleteCommentLike(comment.commentId) },
                                onReply = { viewModel.openReplySheet() },
                                onUserClick = { onUserClick(comment.usuario.usuarioId) }
                            )
                            Spacer(modifier = Modifier.height(28.dp))
                        }

                        item {
                            RespuestasHeader(modifier = Modifier.padding(bottom = 16.dp))
                        }

                        items(uiState.replies) { reply ->
                            ReplyCard(
                                reply = reply,
                                onUserClick = { onUserClick(reply.usuario.usuarioId) },
                                onClick = { onCommentClick(reply.commentId) }
                            )
                        }
                    }
                }
            }

            if (uiState.selectedComment == null && uiState.errorMessage != null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = uiState.errorMessage ?: "", color = Color.Red)
                }
            }
        }
    }
}

@Composable
private fun CommentMainCard(
    comment: CommentInfo,
    onLike: () -> Unit,
    onReply: () -> Unit,
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
            ComentarioContent(
                comment = comment,
                onUserClick = onUserClick,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            CommentActionBar(
                isLiked = comment.liked,
                onLike = onLike,
                onReply = onReply,
                onShare = {}
            )
        }
    }
}

@Composable
fun ComentarioContent(
    comment: CommentInfo,
    onUserClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = comment.usuario.imageprofeUrl,
                contentDescription = null,
                placeholder = painterResource(R.drawable.loading_img),
                error = painterResource(R.drawable.avatar),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(40.dp)
                    .shadow(elevation = 3.dp, shape = CircleShape, clip = false)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), CircleShape)
                    .clickable(onClick = onUserClick)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "@${comment.usuario.nombreUsu}",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.clickable(onClick = onUserClick)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = comment.time,
                        fontSize = 12.sp,
                        color = colorResource(R.color.gris)
                    )
                    if (comment.editado) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = colorResource(R.color.verdetp).copy(alpha = 0.12f)
                        ) {
                            Text(
                                text = "Editado",
                                fontSize = 11.sp,
                                color = colorResource(R.color.verdetp),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }
        Text(
            text = comment.content,
            fontSize = 15.sp,
            modifier = Modifier.padding(start = 2.dp)
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(start = 2.dp, top = 2.dp)
        ) {
            Text(
                text = "${comment.likes} Likes",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.testTag("comment_likes_count")
            )
            Text(
                text = "${comment.repliesCount} Respuestas",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun CommentActionBar(
    isLiked: Boolean,
    onLike: () -> Unit,
    onReply: () -> Unit,
    onShare: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
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

        IconButton(onClick = { likeTriggered = true; onLike() }) {
            Icon(
                imageVector = if (isLiked) Icons.Filled.ThumbUp else Icons.Outlined.ThumbUp,
                contentDescription = "Like",
                tint = colorResource(R.color.verdetp),
                modifier = Modifier.graphicsLayer { scaleX = likeScale; scaleY = likeScale }
            )
        }
        IconButton(onClick = onReply) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.Comment,
                contentDescription = "Responder",
                tint = colorResource(R.color.verdetp)
            )
        }
        IconButton(onClick = onShare) {
            Icon(
                imageVector = Icons.Outlined.Share,
                contentDescription = "Compartir",
                tint = colorResource(R.color.verdetp)
            )
        }
    }
}

@Composable
private fun RespuestasHeader(modifier: Modifier = Modifier) {
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
            text = "Respuestas más relevantes",
            fontSize = 19.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun ReplyCard(
    reply: CommentInfo,
    onUserClick: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .pressScaleEffect()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, colorResource(R.color.BordeTuProfe)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        ComentarioContent(
            comment = reply,
            onUserClick = onUserClick,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentComposeSheet(
    contextText: String,
    contextUser: String,
    text: String,
    onTextChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onSubmit: () -> Unit,
    isSubmitting: Boolean,
    submitLabel: String = "COMENTAR"
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onDismiss) {
                    Text(
                        "Cancelar",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (isSubmitting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = colorResource(R.color.verdetp)
                    )
                } else {
                    Button(
                        onClick = onSubmit,
                        enabled = text.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(R.color.verdetp)
                        ),
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(submitLabel, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (contextText.isNotEmpty()) {
                Row(
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.outlineVariant,
                                    shape = CircleShape
                                )
                        )
                        Box(
                            modifier = Modifier
                                .width(2.dp)
                                .height(40.dp)
                                .background(MaterialTheme.colorScheme.outlineVariant)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Respondiendo a @$contextUser",
                            fontSize = 13.sp,
                            color = colorResource(R.color.verdetp),
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = contextText,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            OutlinedTextField(
                value = text,
                onValueChange = onTextChange,
                placeholder = {
                    Text(
                        "Escribe tu comentario...",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 100.dp),
                maxLines = 8,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                    focusedBorderColor = colorResource(R.color.verdetp)
                ),
                shape = RoundedCornerShape(16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${text.length}/500",
                fontSize = 12.sp,
                color = if (text.length > 450)
                    MaterialTheme.colorScheme.error
                else
                    MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}
