package com.example.tuprofe.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.tuprofe.R
import com.example.tuprofe.data.ChatInfo
import com.example.tuprofe.ui.utils.BackButtonHeader
import com.example.tuprofe.ui.utils.BackgroundImage
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ChatListScreen(
    viewModel: ChatListViewModel = hiltViewModel(),
    onChatClick: (chatId: String, otherUserId: String) -> Unit,
    onBackClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        BackgroundImage()
        Column(modifier = Modifier.fillMaxSize()) {
            BackButtonHeader(onBackClick = onBackClick)

            Text(
                text = stringResource(R.string.chats),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.verdetp),
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
            )

            when {
                state.isLoading -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = colorResource(R.color.verdetp))
                }
                state.chats.isEmpty() -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.sin_mensajes),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 15.sp
                    )
                }
                else -> LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    items(state.chats, key = { it.chatId }) { chat ->
                        ChatListItem(
                            chat = chat,
                            currentUserId = state.currentUserId,
                            onClick = { onChatClick(chat.chatId, chat.otherUserId) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ChatListItem(chat: ChatInfo, currentUserId: String, onClick: () -> Unit) {
    val isUnread = chat.unreadCount > 0
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (isUnread) colorResource(R.color.verdetp).copy(alpha = 0.07f) else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = chat.otherUserImage,
            contentDescription = null,
            placeholder = painterResource(R.drawable.loading_img),
            error = painterResource(R.drawable.avatar),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        )
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = chat.otherUserName.ifEmpty { chat.otherUserId },
                fontWeight = if (isUnread) FontWeight.Bold else FontWeight.SemiBold,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            val sinMensajes = stringResource(R.string.sin_mensajes)
            val tuPrefijo = stringResource(R.string.tu_prefijo)
            val msgWeight = if (isUnread) FontWeight.SemiBold else FontWeight.Normal
            val lastMsgText = buildAnnotatedString {
                if (chat.lastMessage.isEmpty()) {
                    append(sinMensajes)
                } else if (chat.lastMessageSenderId == currentUserId) {
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append(tuPrefijo) }
                    withStyle(SpanStyle(fontWeight = msgWeight)) { append(chat.lastMessage) }
                } else {
                    withStyle(SpanStyle(fontWeight = msgWeight)) { append(chat.lastMessage) }
                }
            }
            Text(
                text = lastMsgText,
                fontSize = 13.sp,
                color = if (isUnread) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Column(horizontalAlignment = Alignment.End) {
            chat.lastMessageAt?.let { date ->
                Text(
                    text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(date),
                    fontSize = 11.sp,
                    fontWeight = if (isUnread) FontWeight.SemiBold else FontWeight.Normal,
                    color = if (isUnread) colorResource(R.color.verdetp) else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (chat.unreadCount > 0) {
                Spacer(Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(colorResource(R.color.verdetp), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = chat.unreadCount.toString(),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = androidx.compose.ui.graphics.Color.White
                    )
                }
            }
        }
    }
    HorizontalDivider(color = colorResource(R.color.BordeTuProfe).copy(alpha = 0.5f))
}
