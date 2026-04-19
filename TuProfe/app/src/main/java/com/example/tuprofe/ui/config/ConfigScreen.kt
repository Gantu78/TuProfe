package com.example.tuprofe.ui.config


import com.example.tuprofe.ui.utils.ProfileHeaderCard
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ThumbsUpDown
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tuprofe.R
import com.example.tuprofe.ui.utils.AppButton
import com.example.tuprofe.ui.utils.BackgroundImage
import com.example.tuprofe.ui.utils.ConfigItem


@Composable
fun ConfigScreen(
    configViewModel: ConfigViewModel,
    onProfileClick: () -> Unit,
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
                            onProfileClick = onProfileClick,
                            showStar = false
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
                subtitle = stringResource(R.string.congiguraci_n_de_la_aplicaci_n),
                onClick = onAjustesClick
            )


    }

}


@Preview (showBackground = true)
@Composable
fun ConfigScreenPreview() {
    ConfigScreen(
        configViewModel = viewModel(),
        onProfileClick = {},
        onLogoutClick = {},
        onCalifClick = {}
    )
}

