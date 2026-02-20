package com.example.tuprofe.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
            //El header
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colorResource(id = R.color.verdetp))
                        .padding(top = 40.dp, bottom = 20.dp)
                ) {
                    //Flecha para ir para atras
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = 10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
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

            //Foto de perfil
            item {
                Spacer(modifier = Modifier.height(30.dp))
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray)
                ) {
                    Image(
                        //Toca cambiar el R.drawable.img por la imagen real del usuario
                        painter = painterResource(id = R.drawable.img),
                        contentDescription = "Foto de perfil",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                AppTextButton(
                    textoBoton = "CAMBIAR FOTO",
                    onClick = { /* logica para poder subir una imagen desde el celular del usuario */ }
                )
            }

            //Formulario
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp)
                ) {
                    TextFieldApp(
                        texto = "CORREO ELECTRÓNICO",
                        value = email,
                        onValueChange = { email = it }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    TextFieldApp(
                        texto = "NOMBRE DE USUARIO",
                        value = username,
                        onValueChange = { username = it }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    TextFieldApp(
                        texto = "CARRERA",
                        value = carrera,
                        onValueChange = { carrera = it }
                    )
                }
            }

            //Botones
            item {
                Spacer(modifier = Modifier.height(25.dp))

                AppTextButton(
                    textoBoton = "CAMBIAR CONTRASEÑA",
                    onClick = { /* Redireccionar a ResetPasswordScreen*/ }
                )

                Spacer(modifier = Modifier.height(10.dp))

                AppButton(
                    textoBoton = "GUARDAR CAMBIOS",
                    onClick = { /*Actualizar Base de datos*/ }
                )

                Spacer(modifier = Modifier.height(30.dp))

                //Botón de borrar cuenta
                Button(
                    onClick = { showDeleteDialog = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red
                    ),
                    modifier = Modifier
                        .width(280.dp)
                        .height(50.dp),
                    shape = RoundedCornerShape(30.dp)
                ) {
                    Text(
                        text = "BORRAR CUENTA",
                        color = Color.White,
                        fontFamily = BebasNeue,
                        fontSize = 20.sp
                    )
                }
                Spacer(modifier = Modifier.height(50.dp))
            }
        }

        //Texto de confirmación de la eliminacion de la cuenta
        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("¿Eliminar cuenta?") },
                text = { Text("Esta acción borrará todos tus datos. No se puede deshacer.") },
                confirmButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("ELIMINAR", color = Color.Red)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("CANCELAR")
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ConfigPerfilPreview() {
    ConfigPerfilScreen()
}