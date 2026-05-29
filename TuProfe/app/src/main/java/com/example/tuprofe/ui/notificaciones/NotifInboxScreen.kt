package com.example.tuprofe.ui.notificaciones

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Comment
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.tuprofe.R
import com.example.tuprofe.data.AppNotification
import com.example.tuprofe.ui.utils.BackgroundImage

@Composable
fun NotifInboxScreen(
    viewModel: NotifInboxViewModel,
    onReviewClick: (String) -> Unit,
    onCommentClick: (String) -> Unit,
    onUserClick: (String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val notifications by viewModel.notifications.collectAsState()

    Box(modifier = modifier.fillMaxSize()) {
        BackgroundImage()

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(bottom = 120.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = null,
                            tint = colorResource(R.color.verdetp)
                        )
                    }
                    Text(
                        text = stringResource(R.string.notificaciones),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier.weight(1f)
                    )
                    if (notifications.any { !it.isRead }) {
                        TextButton(onClick = { viewModel.markAllRead() }) {
                            Text(
                                text = stringResource(R.string.notif_marcar_leidas),
                                color = colorResource(R.color.verdetp),
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }

            if (notifications.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 80.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = null,
                                tint = colorResource(R.color.verdetp).copy(alpha = 0.4f),
                                modifier = Modifier.size(56.dp)
                            )
                            Spacer(Modifier.height(12.dp))
                            Text(
                                text = stringResource(R.string.notif_sin_notificaciones),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 15.sp
                            )
                        }
                    }
                }
            } else {
                items(notifications, key = { it.id }) { notif ->
                    NotifItem(
                        notif = notif,
                        onClick = {
                            viewModel.onNotificationClick(notif)
                            when (notif.type) {
                                "like", "reviewDeleted" -> if (notif.entityId.isNotBlank()) onReviewClick(notif.entityId)
                                "comment", "reply" -> if (notif.entityId.isNotBlank()) onCommentClick(notif.entityId)
                                "follow" -> if (notif.entityId.isNotBlank()) onUserClick(notif.entityId)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun NotifItem(
    notif: AppNotification,
    onClick: () -> Unit
) {
    val unreadBg = if (!notif.isRead)
        colorResource(R.color.verdetp).copy(alpha = 0.06f)
    else
        MaterialTheme.colorScheme.surface

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = unreadBg),
        elevation = CardDefaults.cardElevation(defaultElevation = if (!notif.isRead) 2.dp else 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box {
                if (notif.senderImageUrl.isNotBlank()) {
                    AsyncImage(
                        model = notif.senderImageUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(colorResource(R.color.verdetp).copy(alpha = 0.15f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = typeIcon(notif.type),
                            contentDescription = null,
                            tint = colorResource(R.color.verdetp),
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(colorResource(R.color.verdetp), CircleShape)
                        .align(Alignment.BottomEnd),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = typeIcon(notif.type),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = localizedTitle(notif),
                    fontWeight = if (!notif.isRead) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 14.sp,
                    maxLines = 1
                )
                if (notif.timestamp > 0L) {
                    Text(
                        text = formatRelativeTime(notif.timestamp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp
                    )
                }
            }

            if (!notif.isRead) {
                Spacer(Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(colorResource(R.color.verdetp), CircleShape)
                )
            }
        }
    }
}

@Composable
private fun localizedTitle(notif: AppNotification): String {
    val name = notif.senderName.ifBlank { "?" }
    return when (notif.type) {
        "like" -> stringResource(R.string.notif_like_title, name)
        "comment" -> stringResource(R.string.notif_comment_title, name)
        "reply" -> stringResource(R.string.notif_reply_title, name)
        "follow" -> stringResource(R.string.notif_follow_title, name)
        "reviewDeleted" -> stringResource(R.string.notif_review_deleted_title)
        else -> notif.title.ifBlank { notif.type }
    }
}

private fun typeIcon(type: String): ImageVector = when (type) {
    "like" -> Icons.Outlined.ThumbUp
    "comment", "reply" -> Icons.AutoMirrored.Outlined.Comment
    "follow" -> Icons.Default.PersonAdd
    "review", "reviewDeleted" -> Icons.Default.Star
    else -> Icons.Default.Notifications
}

private fun formatRelativeTime(timestamp: Long): String {
    if (timestamp == 0L) return ""
    return android.text.format.DateUtils.getRelativeTimeSpanString(
        timestamp,
        System.currentTimeMillis(),
        android.text.format.DateUtils.SECOND_IN_MILLIS,
        android.text.format.DateUtils.FORMAT_ABBREV_RELATIVE
    ).toString()
}