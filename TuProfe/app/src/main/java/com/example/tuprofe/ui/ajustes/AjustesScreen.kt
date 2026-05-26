package com.example.tuprofe.ui.ajustes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.ui.draw.alpha
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonOff
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VisibilityOff
import com.example.tuprofe.data.repository.BlockedUser
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tuprofe.HeaderSection
import com.example.tuprofe.R
import com.example.tuprofe.ui.utils.BackgroundImage

@Composable
fun AjustesScreen(
    viewModel: AjustesViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val state by viewModel.uiState.collectAsState()
    val versionName = context.packageManager
        .getPackageInfo(context.packageName, 0).versionName

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
                    title = stringResource(R.string.ajustes),
                    onBackClick = onBackClick
                )
            }

            // ── Sección: Privacidad ───────────────────────────────────────────
            item {
                SectionLabel(stringResource(R.string.privacidad))
                Spacer(Modifier.height(8.dp))
            }

            item {
                PrivacyToggleItem(
                    icon = Icons.Default.VisibilityOff,
                    title = stringResource(R.string.perfil_anonimo),
                    subtitle = stringResource(R.string.perfil_anonimo_desc),
                    checked = state.perfilAnonimo,
                    onCheckedChange = { viewModel.togglePerfilAnonimo() }
                )
            }

            item {
                PrivacyToggleItem(
                    icon = Icons.Default.Person,
                    title = stringResource(R.string.perfil_publico),
                    subtitle = stringResource(R.string.perfil_publico_desc),
                    checked = state.perfilPublico,
                    onCheckedChange = { viewModel.togglePerfilPublico() }
                )
            }

            item {
                PrivacyToggleItem(
                    icon = Icons.Default.Star,
                    title = stringResource(R.string.resenas_en_perfil),
                    subtitle = stringResource(R.string.resenas_en_perfil_desc),
                    checked = state.resenasEnPerfil && state.perfilPublico,
                    enabled = state.perfilPublico,
                    onCheckedChange = { viewModel.toggleResenasEnPerfil() }
                )
                Spacer(Modifier.height(20.dp))
            }

            item {
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                Spacer(Modifier.height(20.dp))
            }

            // ── Sección: Usuarios bloqueados ──────────────────────────────────
            item {
                SectionLabel("Usuarios bloqueados")
                Spacer(Modifier.height(8.dp))
            }

            if (state.isLoadingBlocked) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = colorResource(R.color.verdetp),
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            } else if (state.blockedUsers.isEmpty()) {
                item {
                    PrivacyInfoCard(
                        icon = Icons.Default.PersonOff,
                        text = "No has bloqueado a ningún usuario"
                    )
                }
            } else {
                items(state.blockedUsers, key = { it.userId }) { user ->
                    BlockedUserCard(
                        user = user,
                        onUnblock = { viewModel.unblockUser(user.userId) }
                    )
                }
            }

            item {
                Spacer(Modifier.height(20.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                Spacer(Modifier.height(20.dp))
            }

            // ── Sección: Política de privacidad ──────────────────────────────
            item {
                SectionLabel(stringResource(R.string.politica_privacidad))
                Spacer(Modifier.height(8.dp))
                PrivacyInfoCard(
                    icon = Icons.Default.Shield,
                    text = stringResource(R.string.politica_privacidad_desc)
                )
                Spacer(Modifier.height(20.dp))
            }

            item {
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                Spacer(Modifier.height(20.dp))
            }

            // ── Sección: Versión ──────────────────────────────────────────────
            item {
                SectionLabel(stringResource(R.string.version_app))
                Spacer(Modifier.height(8.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(1.5.dp, colorResource(R.color.BordeTuProfe)),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 20.dp, vertical = 16.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = colorResource(R.color.verdetp),
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(Modifier.width(16.dp))
                        Column {
                            Text(
                                text = "TuProfe",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                            Text(
                                text = "v$versionName",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        color = MaterialTheme.colorScheme.onSurface
    )
}

@Composable
private fun PrivacyToggleItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: () -> Unit,
    enabled: Boolean = true
) {
    val alpha = if (enabled) 1f else 0.4f
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .alpha(alpha),
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
                onCheckedChange = { if (enabled) onCheckedChange() },
                enabled = enabled,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.surface,
                    checkedTrackColor = colorResource(R.color.verdetp)
                )
            )
        }
    }
}

@Composable
private fun BlockedUserCard(user: BlockedUser, onUnblock: () -> Unit) {
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
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = colorResource(R.color.verdetp),
                modifier = Modifier.size(28.dp)
            )
            Spacer(Modifier.width(16.dp))
            Text(
                text = user.username,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                modifier = Modifier.weight(1f)
            )
            OutlinedButton(
                onClick = onUnblock,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.error),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "Desbloquear",
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Composable
private fun PrivacyInfoCard(icon: ImageVector, text: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.5.dp, colorResource(R.color.BordeTuProfe)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = colorResource(R.color.verdetp))
            Spacer(Modifier.width(12.dp))
            Text(text, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
