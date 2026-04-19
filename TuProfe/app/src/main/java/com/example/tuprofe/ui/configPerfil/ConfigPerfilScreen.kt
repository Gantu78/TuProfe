package com.example.tuprofe.ui.configPerfil

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.tuprofe.R
import com.example.tuprofe.ui.theme.BebasNeue
import com.example.tuprofe.ui.utils.*
import android.net.Uri
import androidx.compose.ui.text.style.TextAlign

@Composable
fun ConfigPerfilScreen(
    configPerfilViewModel: ConfigPerfilViewModel,
    onChangePassword: () -> Unit,
    onSaveSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier,

) {
    val state by configPerfilViewModel.uiState.collectAsState()

    LaunchedEffect(state.navigate) {
        if (state.navigate) {
            onNavigateToLogin()
            configPerfilViewModel.onNavigationDone()
        }
    }

    LaunchedEffect(state.saveSuccess) {
        if (state.saveSuccess) {
            configPerfilViewModel.onSaveSuccessDone()
            onSaveSuccess()
        }
    }


    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
        ) {
        BackgroundImage()


        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(
                        text = state.errorMessagePerfil ?: "",
                        color = Color.Red,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    ProfilePicture(
                        imageUrl = state.profileImage,
                        onImageSelected = { uri ->
                            configPerfilViewModel.uploadImageToFirebase(uri)
                        }
                    )
                }

            }

            item {
                UserInfoForm(
                    email = state.email,
                    onEmailChange = { configPerfilViewModel.setEmail(it) },
                    username = state.username,
                    onUsernameChange = { configPerfilViewModel.setUsuario(it) },
                    carrera = state.carrera,
                    onCarreraChange = { configPerfilViewModel.setCarrera(it) }
                )
            }

            item {
                ActionButtons(
                    onCambiarContrasenaClick = onChangePassword,
                    onGuardarCambiosClick = { configPerfilViewModel.toggleShowSave() },
                    onBorrarCuentaClick = { configPerfilViewModel.toggleShowDelete() }
                )
            }
        }

        if (state.showDeleteDialog) {
            DeleteConfirmationDialog(
                errorMessage = state.errorMessageEliminar,
                onDismissRequest = {
                    configPerfilViewModel.toggleShowDelete()
                    configPerfilViewModel.clearErrorEliminar()
                },
                onConfirm = { password ->
                    configPerfilViewModel.clearErrorEliminar()
                    configPerfilViewModel.onBorrarCuentaClick(
                        state.email,
                        password
                    )
                },
            )
        }

        if (state.showSaveDialog) {
            SaveConfirmationDialog(
                onDismissRequest = { configPerfilViewModel.toggleShowSave() },
                onConfirm = {
                    configPerfilViewModel.onGuardarCambiosClick()
                }
            )
        }

        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
private fun DeleteConfirmationDialog(
    errorMessage: String?,
    onDismissRequest: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val icono = if (passwordVisible) R.drawable.mostrar else R.drawable.ocultar

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(stringResource(R.string.eliminar_cuenta)) },
        text = {
            Column {
                Text(stringResource(R.string.ingresa_tu_contrase_a_para_confirmar))

                Spacer(modifier = Modifier.height(8.dp))

                TextFieldContraApp(
                    texto = stringResource(R.string.contrase_a),
                    value = password,
                    onValueChange = { password = it },
                    mostrarPassword = passwordVisible,
                    click = { passwordVisible = !passwordVisible },
                    icono = icono,
                    modifier = Modifier.fillMaxWidth()
                )

                if (!errorMessage.isNullOrEmpty()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = errorMessage,
                        color = Color.Red,
                        fontSize = 12.sp
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(password) },
                enabled = password.isNotBlank()
            ) {
                Text(stringResource(R.string.eliminar), color = Color.Red)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(stringResource(R.string.cancelar))
            }
        }
    )
}

@Composable
private fun SaveConfirmationDialog(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(stringResource(R.string.guardar_cambios)) },
        text = { Text(stringResource(R.string.deseas_guardar_los_cambios)) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(stringResource(R.string.guardar))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(stringResource(R.string.cancelar))
            }
        }
    )
}

@Composable
private fun ProfilePicture(
    imageUrl: String?,
    onImageSelected: (Uri) -> Unit,
) {
    Spacer(modifier = Modifier.height(30.dp))

    Box(
        modifier = Modifier
            .size(150.dp)
            .clip(CircleShape)
            .background(Color.LightGray)
    ) {

        AsyncImage(
            model = imageUrl,
            contentDescription = stringResource(R.string.foto_de_perfil),
            placeholder = painterResource(R.drawable.loading_img),
            error = painterResource(R.drawable.avatar),
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }

    CambiarFoto(action = onImageSelected)
}

@Composable
private fun CambiarFoto(
    action: (Uri) -> Unit,
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { action(it) }
    }

    AppTextButton(
        textoBoton = stringResource(R.string.cambiar_foto),
        onClick = {
            launcher.launch("image/*")
        }
    )
}

@Composable
private fun UserInfoForm(
    email: String,
    onEmailChange: (String) -> Unit,
    username: String,
    onUsernameChange: (String) -> Unit,
    carrera: String,
    onCarreraChange: (String) -> Unit
) {
    Spacer(modifier = Modifier.height(10.dp))
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextFieldApp(
            texto = stringResource(R.string.email),
            value = email,
            onValueChange = onEmailChange
        )
        Spacer(modifier = Modifier.height(10.dp))
        TextFieldApp(
            texto = stringResource(R.string.usuario),
            value = username,
            onValueChange = onUsernameChange
        )
        Spacer(modifier = Modifier.height(10.dp))
        TextFieldApp(
            texto = stringResource(R.string.carrera),
            value = carrera,
            onValueChange = onCarreraChange
        )
    }
}

@Composable
private fun ActionButtons(
    onCambiarContrasenaClick: () -> Unit,
    onGuardarCambiosClick: () -> Unit,
    onBorrarCuentaClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(25.dp))

        AppTextButton(
            textoBoton = stringResource(R.string.cambiar_contrase_a),
            onClick = onCambiarContrasenaClick
        )

        Spacer(modifier = Modifier.height(10.dp))

        AppButton(
            textoBoton = stringResource(R.string.guardar_cambios),
            onClick = onGuardarCambiosClick
        )

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = onBorrarCuentaClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red
            ),
            modifier = Modifier
                .width(110.dp)
                .height(40.dp)
        ) {
            Text(
                text = stringResource(R.string.borrar_cuenta),
                color = Color.White,
                fontFamily = BebasNeue,
                fontSize = 12.sp
            )
        }
        Spacer(modifier = Modifier.height(50.dp))
    }
}

@Composable
@Preview (showBackground = true)
fun ActionIconButtonsPreview() {
    ActionButtons(
        onCambiarContrasenaClick = {},
        onGuardarCambiosClick = {},
        onBorrarCuentaClick = {}

    )
}

@Composable
@Preview(showBackground = true)
fun ProfilePicturePreview() {
    ProfilePicture(
        imageUrl = null,
        onImageSelected = {}
    )
}


@Composable
@Preview(showBackground = true)
fun UserInfoFormPreview() {
    UserInfoForm(
        email = "c.jimenez@javeriana.edu.co",
        onEmailChange = {},
        username = "Gantu970",
        onUsernameChange = {},
        carrera = "Ingenieria Mecatrónica",
        onCarreraChange = {}
    )
}



@Preview(showBackground = true)
@Composable
fun ConfigPerfilPreview() {
    ConfigPerfilScreen(
        configPerfilViewModel = viewModel(),
        onChangePassword = {},
        onSaveSuccess = {},
        onNavigateToLogin = {}
    )
}
