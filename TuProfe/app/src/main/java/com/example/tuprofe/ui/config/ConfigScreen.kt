package com.example.tuprofe.ui.config


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ThumbsUpDown
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.tuprofe.R
import com.example.tuprofe.data.Usuario
import com.example.tuprofe.ui.utils.BackgroundImage
import com.example.tuprofe.ui.utils.ConfigItem
import com.example.tuprofe.ui.utils.TpIconButton


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfigScreen(
    configViewModel: ConfigViewModel,
    onEditProfileClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onCalifClick: () -> Unit,
    onAyudaClick: () -> Unit = {},
    onNotificacionesClick: () -> Unit = {},
    onAjustesClick: () -> Unit = {},
    onSuscripcionClick: () -> Unit = {},
    onChatListClick: () -> Unit = {},
    onUserClick: (String) -> Unit = {},
    onNotifInboxClick: () -> Unit = {},
    modifier: Modifier = Modifier.testTag("profileScreen")
) {


       val state by configViewModel.uiState.collectAsState()



    LaunchedEffect(Unit) {
        configViewModel.loadUserProfile()
    }

    when {
        state.isLoading -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        else -> {
            Box(
                modifier = modifier
                    .fillMaxSize()
            ) {

                BackgroundImage()

                Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box {
                        TpIconButton(
                            icon = Icons.Default.Notifications,
                            contentDescription = stringResource(R.string.notificaciones),
                            onClick = onNotifInboxClick
                        )
                        if (state.unreadNotifCount > 0) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .background(Color.Red, CircleShape)
                                    .align(Alignment.TopEnd)
                            )
                        }
                    }
                    Box {
                        TpIconButton(
                            icon = Icons.Default.Chat,
                            contentDescription = stringResource(R.string.chat),
                            onClick = onChatListClick
                        )
                        if (state.hasUnreadChats) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .background(Color.Red, CircleShape)
                                    .align(Alignment.TopEnd)
                            )
                        }
                    }
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 30.dp),
                    contentPadding = PaddingValues(
                        bottom = 120.dp
                    )
                ) {

                    item {
                        CenteredProfileHeader(
                            username = state.username,
                            email = state.email,
                            carrera = state.carrera,
                            imageUrl = state.profileImageUrl,
                            followersCount = state.followersCount,
                            followingCount = state.followingCount,
                            onFollowersClick = { configViewModel.openFollowersSheet() },
                            onFollowingClick = { configViewModel.openFollowingSheet() }
                        )
                    }

                    item { Spacer(modifier = Modifier.height(24.dp)) }

                    item {
                        ConfigBody(
                            onEditProfileClick = onEditProfileClick,
                            onCalifClick = onCalifClick,
                            onAyudaClick = onAyudaClick,
                            onNotificacionesClick = onNotificacionesClick,
                            onAjustesClick = onAjustesClick,
                            modifier = Modifier
                        )
                    }

                    item { Spacer(modifier = Modifier.height(20.dp)) }
                    item {
                        when {
                            // Suscripción activa con más de 3 días → no mostrar nada
                            state.subscriptionActive && (state.subscriptionDaysLeft ?: 0) > 3 -> { }

                            // Suscripción por vencer (3 días o menos) → recordatorio
                            state.subscriptionActive && (state.subscriptionDaysLeft ?: 0) <= 3 -> {
                                RecordatorioVencimiento(
                                    diasRestantes = state.subscriptionDaysLeft ?: 0,
                                    onClick = onSuscripcionClick
                                )
                            }

                            // Sin suscripción → banner normal
                            else -> {
                                SuscripcionBanner(onClick = onSuscripcionClick)
                            }
                        }
                    }
                    item {
                        OutlinedButton(
                            onClick = onLogoutClick,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(1.dp, Color.Red.copy(alpha = 0.3f)),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = Color.Red.copy(alpha = 0.08f),
                                contentColor = Color.Red
                            )
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                contentDescription = null,
                                tint = Color.Red,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = stringResource(R.string.cerrar_sesi_n),
                                color = Color.Red,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
                } // end Column
            }
        }
    }

    val showSheet = state.showFollowersSheet || state.showFollowingSheet
    if (showSheet) {
        val title = if (state.showFollowersSheet) stringResource(R.string.seguidores) else stringResource(R.string.siguiendo)
        val list = if (state.showFollowersSheet) state.followersList else state.followingList

        ModalBottomSheet(onDismissRequest = { configViewModel.closeSheet() }) {
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
                        text = stringResource(R.string.no_hay_usuarios),
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
                    items(list, key = { it.usuarioId }) { usuario ->
                        ConfigUserListItem(
                            usuario = usuario,
                            onFollowClick = { configViewModel.followOrUnfollowInList(usuario.usuarioId) },
                            onUserClick = { onUserClick(usuario.usuarioId) }
                        )
                    }
                }
            }
        }
    }
}



@Composable
private fun ConfigUserListItem(
    usuario: Usuario,
    onFollowClick: () -> Unit,
    onUserClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onUserClick)
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
        OutlinedButton(
            onClick = onFollowClick,
            border = BorderStroke(1.5.dp, colorResource(R.color.verdetp)),
            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 4.dp),
            modifier = Modifier.height(34.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = if (usuario.followed) colorResource(R.color.verdetp) else Color.Transparent,
                contentColor = if (usuario.followed) Color.White else colorResource(R.color.verdetp)
            )
        ) {
            Text(
                text = if (usuario.followed) stringResource(R.string.siguiendo) else stringResource(R.string.seguir),
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun CenteredProfileHeader(
    username: String,
    email: String,
    carrera: String,
    imageUrl: String?,
    followersCount: Int,
    followingCount: Int,
    onFollowersClick: () -> Unit,
    onFollowingClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            placeholder = painterResource(R.drawable.loading_img),
            error = painterResource(R.drawable.avatar),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(90.dp)
                .border(3.dp, colorResource(R.color.verdetp), CircleShape)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = username,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = carrera,
            fontSize = 14.sp,
            color = colorResource(R.color.verdetp)
        )
        Spacer(Modifier.height(2.dp))
        Text(
            text = email,
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(16.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(40.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable(onClick = onFollowersClick)
            ) {
                Text(
                    text = followersCount.toString(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = stringResource(R.string.seguidores),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            VerticalDivider(modifier = Modifier.height(32.dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable(onClick = onFollowingClick)
            ) {
                Text(
                    text = followingCount.toString(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = stringResource(R.string.siguiendo),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun ConfigBody(
    onEditProfileClick: () -> Unit,
    onCalifClick: () -> Unit,
    onAyudaClick: () -> Unit,
    onNotificacionesClick: () -> Unit,
    onAjustesClick: () -> Unit,
    modifier: Modifier
) {
    Column(modifier = modifier) {
        ConfigItem(
            icon = Icons.Default.Edit,
            title = stringResource(R.string.editar_perfil),
            subtitle = stringResource(R.string.editar_perfil_sub),
            onClick = onEditProfileClick
        )
        ConfigItem(
            icon = Icons.Default.ThumbsUpDown,
            title = stringResource(R.string.historial_de_calificaciones),
            subtitle = stringResource(R.string.qu_profes_has_calificado),
            onClick = onCalifClick
        )
        ConfigItem(
            icon = Icons.Default.Notifications,
            title = stringResource(R.string.notificaciones),
            subtitle = stringResource(R.string.alertas_y_novedades),
            onClick = onNotificacionesClick
        )
        ConfigItem(
            icon = Icons.Default.Settings,
            title = stringResource(R.string.ajustes),
            subtitle = stringResource(R.string.configuraci_n_de_la_aplicaci_n),
            onClick = onAjustesClick
        )
        ConfigItem(
            icon = Icons.Default.MailOutline,
            title = stringResource(R.string.ayuda_y_soporte),
            subtitle = stringResource(R.string.faq_t_rminos_y_condiciones),
            onClick = onAyudaClick
        )
    }
}
@Composable
fun RecordatorioVencimiento(diasRestantes: Int, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        ),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (diasRestantes == 0) stringResource(R.string.suscripcion_vence_hoy)
                    else stringResource(R.string.suscripcion_vence_dias, diasRestantes),
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = stringResource(R.string.renueva_acceso),
                    color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.85f),
                    fontSize = 13.sp
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onErrorContainer,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
@Composable
fun SuscripcionBanner(onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.verdetp)
        ),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.ayudanos_mejorar),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = stringResource(R.string.apoya_tuprofe_precio),
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 13.sp
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ConfigScreenPreview() {
    ConfigScreen(
        configViewModel = viewModel(),
        onEditProfileClick = {},
        onLogoutClick = {},
        onCalifClick = {}
    )
}

