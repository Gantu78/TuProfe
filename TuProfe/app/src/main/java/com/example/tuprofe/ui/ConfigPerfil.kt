package com.example.tuprofe.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tuprofe.R
import com.example.tuprofe.ui.theme.BebasNeue
import com.example.tuprofe.ui.utils.*

@Composable
fun ConfigPerfilScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = { println("Action: Navigate back") }
) {
    var email by remember { mutableStateOf("c.jimenez@javeriana.edu.co") }
    var username by remember { mutableStateOf("Gantu970") }
    var carrera by remember { mutableStateOf("Ingenieria Mecatrónica") }
    var showDeleteDialog by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize()) {
        BackgroundImage()

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                ConfigPerfilHeader(onBackClick = onBackClick)
            }

            item {
                ProfilePicture(
                    onCambiarFotoClick = { println("Action: Change profile picture") }
                )
            }

            item {
                UserInfoForm(
                    email = email,
                    onEmailChange = { email = it },
                    username = username,
                    onUsernameChange = { username = it },
                    carrera = carrera,
                    onCarreraChange = { carrera = it }
                )
            }

            item {
                ActionButtons(
                    onCambiarContrasenaClick = { println("Action: Navigate to change password") },
                    onGuardarCambiosClick = { println("Action: Save user profile changes for user $username") },
                    onBorrarCuentaClick = { showDeleteDialog = true }
                )
            }
        }

        if (showDeleteDialog) {
            DeleteConfirmationDialog(
                onDismissRequest = { showDeleteDialog = false },
                onConfirm = {
                    showDeleteDialog = false
                    println("Action: Delete account for user $username")
                }
            )
        }
    }
}

@Composable
private fun ConfigPerfilHeader(onBackClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(id = R.color.verdetp))
            .padding(top = 10.dp, bottom = 10.dp)
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 10.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.atras),
                tint = Color.White
            )
        }

        Text(
            text = stringResource(R.string.perfil),
            fontSize = 35.sp,
            fontFamily = BebasNeue,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
private fun ProfilePicture(onCambiarFotoClick: () -> Unit) {
    Spacer(modifier = Modifier.height(30.dp))
    Box(
        modifier = Modifier
            .size(150.dp)
            .clip(CircleShape)
            .background(Color.LightGray)
    ) {
        Image(
            painter = painterResource(id = R.drawable.avatar),
            contentDescription = stringResource(R.string.foto_de_perfil),
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
    AppTextButton(
        textoBoton = stringResource(R.string.cambiar_foto),
        onClick = onCambiarFotoClick
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
private fun DeleteConfirmationDialog(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(stringResource(R.string.borrar_cuenta)) },
        text = { Text(stringResource(R.string.confirmacionBorrar)) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(stringResource(R.string.borrar_cuenta), color = Color.Red)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(stringResource(R.string.cancelar))
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun ConfigPerfilPreview() {
    ConfigPerfilScreen()
}
