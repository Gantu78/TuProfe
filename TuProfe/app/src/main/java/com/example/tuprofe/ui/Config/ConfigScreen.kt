package com.example.tuprofe.ui.Config


import com.example.tuprofe.ui.utils.ProfileHeaderCard
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.ThumbsUpDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
                        username = "Gantu870",
                        email = "c.jimenez@javeriana.edu.co",
                        carrera = "Ing. de Sistemas",
                        imageRes = R.drawable.carlitos,
                        onProfileClick = onProfileClick,
                        showStar = false
                    )
                }

                item { Spacer(modifier = Modifier.height(20.dp)) }



                item {
                    ConfigItem(
                        icon = Icons.Default.ThumbsUpDown,
                        title = stringResource(R.string.historial_de_calificaciones),
                        subtitle = stringResource(R.string.qu_profes_has_calificado),
                        onClick = onCalifClick
                    )
                }

                item {
                    ConfigItem(
                        icon = Icons.Default.MailOutline,
                        title = stringResource(R.string.ayuda_y_soporte),
                        subtitle = stringResource(R.string.faq_t_rminos_y_condiciones),
                        onClick = {configViewModel.onAyudaClick()}
                    )
                }

                item {
                    ConfigItem(
                        icon = Icons.Default.Lock,
                        title = stringResource(R.string.privacidad),
                        subtitle = stringResource(R.string.perfil_an_nimo_visibilidad),
                        onClick = {configViewModel.onPrivacidadClick()}
                    )
                }

                item {
                    ConfigItem(
                        icon = Icons.Default.Notifications,
                        title = stringResource(R.string.notificaciones),
                        subtitle = stringResource(R.string.alertas_y_novedades),
                        onClick = {configViewModel.onNotificacionesClick()}
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


@Composable
fun UserCard(
    username: String,
    email: String
) {
    Card (
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(2.dp, colorResource(R.color.BordeTuProfe)),
        colors = CardDefaults.cardColors(
            MaterialTheme.colorScheme.surface
        ),
    ) {
        Row (
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = Icons.Default.MailOutline,
                contentDescription = null,
                tint = colorResource(R.color.verdetp),
                modifier = Modifier.size(40.dp)
            )

            Spacer(modifier = Modifier.width(20.dp))


            Column() {
                Text(username, fontWeight = FontWeight.Bold)
                Text(email, color = Color.Gray)

            }
        }
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

