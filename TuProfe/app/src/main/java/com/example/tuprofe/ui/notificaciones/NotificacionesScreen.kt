package com.example.tuprofe.ui.notificaciones

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material.icons.automirrored.outlined.Comment
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tuprofe.HeaderSection
import com.example.tuprofe.R
import com.example.tuprofe.ui.utils.BackgroundImage

@Composable
fun NotificacionesScreen(
    viewModel: NotificacionesViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        viewModel.onPermissionResult(granted)
    }

    LaunchedEffect(Unit) {
        viewModel.refreshPermissionState()
    }

    Box(modifier = modifier.fillMaxSize()) {
        BackgroundImage()

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            item {
                HeaderSection(
                    title = stringResource(R.string.notificaciones),
                    onBackClick = onBackClick
                )
            }

            item {
                NotifToggleItem(
                    icon = Icons.Outlined.ThumbUp,
                    title = stringResource(R.string.likes_resenas),
                    subtitle = stringResource(R.string.likes_resenas_desc),
                    checked = state.likesEnabled,
                    onCheckedChange = { viewModel.toggleLikes() }
                )
            }

            item {
                NotifToggleItem(
                    icon = Icons.AutoMirrored.Outlined.Comment,
                    title = stringResource(R.string.comentarios_resenas),
                    subtitle = stringResource(R.string.comentarios_resenas_desc),
                    checked = state.comentariosEnabled,
                    onCheckedChange = { viewModel.toggleComentarios() }
                )
            }

            item {
                NotifToggleItem(
                    icon = Icons.Default.PersonAdd,
                    title = stringResource(R.string.nuevos_seguidores),
                    subtitle = stringResource(R.string.nuevos_seguidores_desc),
                    checked = state.seguidoresEnabled,
                    onCheckedChange = { viewModel.toggleSeguidores() }
                )
                Spacer(Modifier.height(20.dp))
            }

            item {
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                Spacer(Modifier.height(20.dp))
            }

            item {
                SystemPermissionCard(
                    granted = state.permissionGranted,
                    onRequest = {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun NotifToggleItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.5.dp, colorResource(R.color.BordeTuProfe)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 14.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = colorResource(R.color.verdetp),
                modifier = Modifier.size(28.dp)
            )
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text(
                    subtitle,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp
                )
            }
            Spacer(Modifier.width(16.dp))
            Switch(
                checked = checked,
                onCheckedChange = { onCheckedChange() },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.surface,
                    checkedTrackColor = colorResource(R.color.verdetp)
                )
            )
        }
    }
}

@Composable
private fun SystemPermissionCard(granted: Boolean, onRequest: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.5.dp, colorResource(R.color.BordeTuProfe)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = if (granted) Icons.Default.NotificationsActive else Icons.Default.Notifications,
                    contentDescription = null,
                    tint = colorResource(R.color.verdetp),
                    modifier = Modifier.size(28.dp)
                )
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(
                        text = stringResource(R.string.activar_permisos),
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Text(
                        text = stringResource(R.string.activar_permisos_desc),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp
                    )
                }
            }
            if (!granted) {
                Spacer(Modifier.height(12.dp))
                Button(
                    onClick = onRequest,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.verdetp)
                    )
                ) {
                    Text(stringResource(R.string.activar_permisos), fontWeight = FontWeight.Bold)
                }
            } else {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.permiso_otorgado),
                    color = colorResource(R.color.verdetp),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                )
            }
        }
    }
}
