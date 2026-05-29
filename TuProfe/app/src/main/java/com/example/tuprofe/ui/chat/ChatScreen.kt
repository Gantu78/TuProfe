package com.example.tuprofe.ui.chat

import java.text.SimpleDateFormat
import java.util.Locale
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.tuprofe.R
import com.example.tuprofe.data.Message
import com.example.tuprofe.ui.utils.BackButtonHeader
import com.example.tuprofe.ui.utils.BackgroundImage
import com.example.tuprofe.ui.utils.FullScreenImageViewer

@Composable
fun ChatScreen(
    viewModel: ChatViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onUserClick: (String) -> Unit = {}
) {
    val state by viewModel.uiState.collectAsState()
    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> viewModel.setPendingImage(uri) }

    LaunchedEffect(state.messages.size) {
        if (state.messages.isNotEmpty()) {
            listState.animateScrollToItem(state.messages.size - 1)
            viewModel.markAsRead()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        BackgroundImage()
        Column(modifier = Modifier.fillMaxSize()) {
            BackButtonHeader(onBackClick = onBackClick)

            if (state.otherUserName.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onUserClick(viewModel.otherUserId) }
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = state.otherUserImage,
                        contentDescription = null,
                        placeholder = painterResource(R.drawable.loading_img),
                        error = painterResource(R.drawable.avatar),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(
                        text = state.otherUserName,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                HorizontalDivider(color = colorResource(R.color.BordeTuProfe))
            }

            when {
                state.isLoading -> Box(Modifier.weight(1f), Alignment.Center) {
                    CircularProgressIndicator(color = colorResource(R.color.verdetp))
                }
                state.messages.isEmpty() -> Box(Modifier.weight(1f), Alignment.Center) {
                    Text(stringResource(R.string.sin_mensajes), color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
                }
                else -> LazyColumn(
                    state = listState,
                    modifier = Modifier.weight(1f).padding(horizontal = 12.dp),
                    contentPadding = PaddingValues(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(state.messages, key = { it.messageId }) { message ->
                        MessageBubble(message = message, isOwn = message.senderId == state.currentUserId)
                    }
                }
            }

            // Pending image preview
            if (state.pendingImageUri != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    AsyncImage(
                        model = state.pendingImageUri,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .border(2.dp, colorResource(R.color.BordeTuProfe), RoundedCornerShape(10.dp))
                    )
                    IconButton(
                        onClick = { viewModel.setPendingImage(null) },
                        modifier = Modifier
                            .size(24.dp)
                            .offset(x = 68.dp, y = (-4).dp)
                            .background(MaterialTheme.colorScheme.errorContainer, CircleShape)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = null,
                            tint = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.size(14.dp))
                    }
                }
            }

            // Input bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 8.dp, vertical = 8.dp)
                    .navigationBarsPadding()
                    .imePadding(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { imagePicker.launch("image/*") },
                    modifier = Modifier.size(42.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AddPhotoAlternate,
                        contentDescription = stringResource(R.string.adjuntar_imagen),
                        tint = colorResource(R.color.verdetp),
                        modifier = Modifier.size(26.dp)
                    )
                }
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = { Text(stringResource(R.string.escribe_un_mensaje), fontSize = 14.sp) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(24.dp),
                    maxLines = 4,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colorResource(R.color.verdetp),
                        unfocusedBorderColor = colorResource(R.color.BordeTuProfe)
                    )
                )
                Spacer(Modifier.width(6.dp))
                val canSend = (inputText.isNotBlank() || state.pendingImageUri != null) && !state.isSending
                IconButton(
                    onClick = {
                        viewModel.sendMessage(inputText)
                        inputText = ""
                    },
                    enabled = canSend,
                    modifier = Modifier
                        .size(46.dp)
                        .background(
                            if (canSend) colorResource(R.color.verdetp) else colorResource(R.color.verdetp).copy(alpha = 0.4f),
                            CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = stringResource(R.string.enviar),
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun MessageBubble(message: Message, isOwn: Boolean) {
    val bubbleColor = if (isOwn) colorResource(R.color.verdetp) else colorResource(R.color.pastel)
    val textColor = if (isOwn) Color.White else MaterialTheme.colorScheme.onSurface
    val shape = RoundedCornerShape(
        topStart = 16.dp, topEnd = 16.dp,
        bottomStart = if (isOwn) 16.dp else 4.dp,
        bottomEnd = if (isOwn) 4.dp else 16.dp
    )
    var showViewer by remember { mutableStateOf(false) }

    if (showViewer && message.imageUrl != null) {
        FullScreenImageViewer(imageUrl = message.imageUrl, onDismiss = { showViewer = false })
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isOwn) Arrangement.End else Arrangement.Start
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 260.dp)
                .background(bubbleColor, shape)
                .padding(if (message.imageUrl != null) 6.dp else 0.dp),
            horizontalAlignment = if (isOwn) Alignment.End else Alignment.Start
        ) {
            if (message.imageUrl != null) {
                AsyncImage(
                    model = message.imageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(R.drawable.loading_img),
                    error = painterResource(R.drawable.loading_img),
                    modifier = Modifier
                        .sizeIn(minWidth = 160.dp, minHeight = 120.dp, maxWidth = 240.dp, maxHeight = 220.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { showViewer = true }
                )
            }
            if (message.text.isNotBlank()) {
                Row(
                    modifier = Modifier.padding(
                        start = 14.dp, end = 10.dp,
                        top = if (message.imageUrl != null) 6.dp else 8.dp,
                        bottom = 3.dp
                    ),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = message.text,
                        color = textColor,
                        fontSize = 15.sp,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    message.sentAt?.let { date ->
                        Text(
                            text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(date),
                            fontSize = 11.sp,
                            color = if (isOwn) Color.White.copy(alpha = 0.65f) else Color.Gray,
                            modifier = Modifier.offset(y = 3.dp)
                        )
                    }
                }
            } else {
                message.sentAt?.let { date ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 10.dp, bottom = 5.dp, top = 4.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(date),
                            fontSize = 11.sp,
                            color = if (isOwn) Color.White.copy(alpha = 0.65f) else Color.Gray
                        )
                    }
                }
            }
        }
    }
}
