package com.example.tuprofe.ui.config


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ThumbsUpDown
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.tuprofe.R
import com.example.tuprofe.data.Usuario
import com.example.tuprofe.ui.utils.AppButton
import com.example.tuprofe.ui.utils.BackgroundImage
import com.example.tuprofe.ui.utils.ConfigItem
import com.example.tuprofe.ui.utils.ProfileHeaderCard


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfigScreen(
    configViewModel: ConfigViewModel,
    onEditProfileClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onCalifClick: () -> Unit,
    modifier: Modifier = Modifier
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

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 30.dp),
                    contentPadding = PaddingValues(
                        bottom = 120.dp
                    )
                ) {

                    item {
                        ProfileHeaderCard(
                            username = state.username,
                            email = state.email,
                            carrera = state.carrera,
                            imageUrl = state.profileImageUrl,
                            onProfileClick = {},
                            showStar = false,
                            followersCount = state.followersCount,
                            followingCount = state.followingCount,
                            onEditClick = onEditProfileClick,
                            onFollowersClick = { configViewModel.openFollowersSheet() },
                            onFollowingClick = { configViewModel.openFollowingSheet() }
                        )
                    }

                    item { Spacer(modifier = Modifier.height(20.dp)) }

                    item {
                        ConfigBody(
                            onCalifClick = onCalifClick,
                            onAyudaClick = {configViewModel.onAyudaClick()},
                            onPrivacidadClick = {configViewModel.onPrivacidadClick()},
                            onAjustesClick = {configViewModel.onAjustesClick()},
                            modifier = Modifier
                        )
                    }

                    item { Spacer(modifier = Modifier.height(20.dp)) }

                    item {
                        AppButton(
                            textoBoton = stringResource(R.string.cerrar_sesi_n),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 30.dp),
                            onClick = onLogoutClick
                        )
                    }
                }
            }
        }
    }

    val showSheet = state.showFollowersSheet || state.showFollowingSheet
    if (showSheet) {
        val title = if (state.showFollowersSheet) "Seguidores" else "Siguiendo"
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
                        text = "No hay usuarios aún",
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
                            onFollowClick = { configViewModel.followOrUnfollowInList(usuario.usuarioId) }
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
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
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
                text = if (usuario.followed) "Siguiendo" else "Seguir",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun ConfigBody(
    onCalifClick: () -> Unit,
    onAyudaClick: () -> Unit,
    onPrivacidadClick: () -> Unit,
    onAjustesClick: () -> Unit,
    modifier: Modifier
){
    Column(
        modifier = modifier,

    ) {

            ConfigItem(
                icon = Icons.Default.ThumbsUpDown,
                title = stringResource(R.string.historial_de_calificaciones),
                subtitle = stringResource(R.string.qu_profes_has_calificado),
                onClick = onCalifClick
            )



            ConfigItem(
                icon = Icons.Default.MailOutline,
                title = stringResource(R.string.ayuda_y_soporte),
                subtitle = stringResource(R.string.faq_t_rminos_y_condiciones),
                onClick = onAyudaClick
            )



            ConfigItem(
                icon = Icons.Default.Lock,
                title = stringResource(R.string.privacidad),
                subtitle = stringResource(R.string.perfil_an_nimo_visibilidad),
                onClick = onPrivacidadClick
            )



            ConfigItem(
                icon = Icons.Default.Settings,
                title = stringResource(R.string.ajustes),
                subtitle = stringResource(R.string.configuraci_n_de_la_aplicaci_n),
                onClick = onAjustesClick
            )


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

