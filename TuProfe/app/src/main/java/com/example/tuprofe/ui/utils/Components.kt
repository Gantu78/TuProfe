package com.example.tuprofe.ui.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.TextButton
import androidx.compose.ui.text.style.TextDecoration
import com.example.tuprofe.R
import com.example.tuprofe.ui.theme.BebasNeue

@Composable
fun LogoApp(
    modifier: Modifier = Modifier
){
    Image(
        painter = painterResource(R.drawable.img),
        contentDescription = stringResource(R.string.logo_tuprofe)
    )
}


@Composable
fun AppButton(
    textoBoton: String,
    modifier: Modifier = Modifier
){
    Button(
        onClick =  {/*TODO*/},
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(R.color.verdetp)
        ),

        modifier = modifier
    ) {
        Text(textoBoton, fontSize = 20.sp, fontFamily = BebasNeue)
    }
}


@Composable
fun AppButtonRow(
    textoBoton: String,
    modifier: Modifier = Modifier
){
    Button(
        onClick =  {/*TODO*/},
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(R.color.verdetp)
        ),

        modifier = modifier
    ) {
        Text(textoBoton, fontSize = 15.sp, fontFamily = BebasNeue)
    }
}






@Composable
fun AppTextButton(
    textoBoton: String,
    modifier: Modifier = Modifier
){
    TextButton(
        onClick = {/*TODO*/}, // Usamos el parámetro
        modifier = modifier
    ) {
        Text(
            text = textoBoton,
            fontSize = 20.sp,
            textDecoration = TextDecoration.Underline, // Subraya el texto
            color = colorResource(R.color.verdetp),
            fontFamily = BebasNeue
        )
    }
}


@Composable
fun TextFieldApp(
    texto: String,
    modifier: Modifier = Modifier
){
    OutlinedTextField(
        value = "",
        onValueChange = {},
        label = { Text(text = texto)},
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = colorResource(R.color.pastel),
            focusedContainerColor = colorResource(R.color.pastel),
            unfocusedLabelColor = colorResource(R.color.gris),
            focusedLabelColor = colorResource(R.color.gris),
            unfocusedIndicatorColor = colorResource(R.color.gris),
            focusedIndicatorColor = colorResource(R.color.gris)
        ),
        shape = RoundedCornerShape(30.dp),
        modifier = modifier
    )
}


@Composable
fun TextFieldContraApp(
    texto: String,
    modifier: Modifier = Modifier,
    visualTransformation: VisualTransformation = PasswordVisualTransformation()
){
    OutlinedTextField(
        value = "",
        onValueChange = {},
        label = { Text(text = texto)},
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = colorResource(R.color.pastel),
            focusedContainerColor = colorResource(R.color.pastel),
            unfocusedLabelColor = colorResource(R.color.gris),
            focusedLabelColor = colorResource(R.color.gris),
            unfocusedIndicatorColor = colorResource(R.color.gris),
            focusedIndicatorColor = colorResource(R.color.gris)
        ),
        shape = RoundedCornerShape(30.dp),
        visualTransformation = visualTransformation,
        trailingIcon = {
            IconButton(onClick = {/*TODO*/}) {
                Icon(
                    painter = painterResource(R.drawable.mostrar_password),
                    contentDescription = stringResource(R.string.mostrar_password),
                    modifier = Modifier.padding(end = 10.dp)
                )
            }
        },
        modifier = Modifier.padding(vertical = 2.dp)
    )
}
