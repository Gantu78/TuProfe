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
    onBackClick: () -> Unit = {}
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
                    onCambiarFotoClick = { /* logica para poder subir una imagen desde el celular del usuario */ }
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
                    onCambiarContrasenaClick = { /* Redireccionar a ResetPasswordScreen*/ },
                    onGuardarCambiosClick = { /*Actualizar Base de datos*/ },
                    onBorrarCuentaClick = { showDeleteDialog = true }
                )
            }
        }

        if (showDeleteDialog) {
            DeleteConfirmationDialog(
                onDismissRequest = { showDeleteDialog = false },
                onConfirm = {
                    showDeleteDialog = false
                    // Lógica para borrar cuenta
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
        //Flecha para ir para atras
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 10.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Atrás",
                tint = Color.White
            )
        }

        //Titulo de la sección "perfil"
        Text(
            text = "PERFIL",
            fontSize = 35.sp,
            fontFamily = BebasNeue,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun ConfigPerfilHeaderPreview() {
    ConfigPerfilHeader(onBackClick = {})
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
            //Toca cambiar el R.drawable.img por la imagen real del usuario
            painter = painterResource(id = R.drawable.avatar),
            contentDescription = "Foto de perfil",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
    AppTextButton(
        textoBoton = "CAMBIAR FOTO",
        onClick = onCambiarFotoClick
    )
}

@Preview(showBackground = true)
@Composable
private fun ProfilePicturePreview() {
    ProfilePicture(onCambiarFotoClick = {})
}

@Composable
private fun UserInfoForm(
    email: String,
    onEmailChange: (String) -> Unit,
    username: String,
    onUsernameChange: (String) -> Unit,
    carrera: String,
    onCarreraChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Spacer(modifier = Modifier.height(10.dp))
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextFieldApp(
            texto = "CORREO ELECTRÓNICO",
            value = email,
            onValueChange = onEmailChange
        )
        Spacer(modifier = Modifier.height(10.dp))
        TextFieldApp(
            texto = "NOMBRE DE USUARIO",
            value = username,
            onValueChange = onUsernameChange
        )
        Spacer(modifier = Modifier.height(10.dp))
        TextFieldApp(
            texto = "CARRERA",
            value = carrera,
            onValueChange = onCarreraChange
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun UserInfoFormPreview() {
    UserInfoForm(
        email = "c.jimenez@javeriana.edu.co",
        onEmailChange = {},
        username = "Gantu780",
        onUsernameChange = { },
        carrera = "Comunicación",
        onCarreraChange = { }
    )
}

@Composable
private fun ActionButtons(
    onCambiarContrasenaClick: () -> Unit,
    onGuardarCambiosClick: () -> Unit,
    onBorrarCuentaClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(25.dp))

        AppTextButton(
            textoBoton = "CAMBIAR CONTRASEÑA",
            onClick = onCambiarContrasenaClick
        )

        Spacer(modifier = Modifier.height(10.dp))

        AppButton(
            textoBoton = "GUARDAR CAMBIOS",
            onClick = onGuardarCambiosClick
        )

        Spacer(modifier = Modifier.height(30.dp))

        //Botón de borrar cuenta
        Button(
            onClick = onBorrarCuentaClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red
            ),
            modifier = Modifier
                .width(110.dp)
                .height(40.dp),

        ) {
            Text(
                text = "BORRAR CUENTA",
                color = Color.White,
                fontFamily = BebasNeue,
                fontSize = 12.sp
            )
        }
        Spacer(modifier = Modifier.height(50.dp))
    }

}
@Preview(showBackground = true)
@Composable
private fun ActionButtonsPreview() {
    ActionButtons(
        onCambiarContrasenaClick = {},
        onGuardarCambiosClick = {},
        onBorrarCuentaClick = {}
    )
}


@Composable
private fun DeleteConfirmationDialog(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("¿Eliminar cuenta?") },
        text = { Text("Esta acción borrará todos tus datos. No se puede deshacer.") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("ELIMINAR", color = Color.Red)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("CANCELAR")
            }
        }
    )
}


@Preview(showBackground = true)
@Composable
fun ConfigPerfilPreview() {
    ConfigPerfilScreen()
}
