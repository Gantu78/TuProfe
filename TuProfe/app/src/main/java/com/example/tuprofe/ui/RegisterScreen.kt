package com.example.tuprofe.ui


import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tuprofe.R
import com.example.tuprofe.ui.theme.BebasNeue
import com.example.tuprofe.ui.utils.AppButton
import com.example.tuprofe.ui.utils.AppTextButton
import com.example.tuprofe.ui.utils.BackgroundImage
import com.example.tuprofe.ui.utils.LogoApp
import com.example.tuprofe.ui.utils.TextFieldApp
import com.example.tuprofe.ui.utils.TextFieldContraApp

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
) {
    var email by remember { mutableStateOf("") }
    var usuario by remember { mutableStateOf("") }
    var carrera by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var password2 by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var passwordVisible2 by remember { mutableStateOf(false) }
    val passwordIcon = if (passwordVisible) R.drawable.mostrar else R.drawable.ocultar
    val password2Icon = if (passwordVisible2) R.drawable.mostrar else R.drawable.ocultar

    Box(
        modifier = modifier
    ) {
        BackgroundImage()
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {

            Header()
            FormularioRegistro(
                email = email,
                usuario = usuario,
                carrera = carrera,
                password = password,
                password2 = password2,
                passwordVisible = passwordVisible,
                passwordVisible2 = passwordVisible2,
                passwordIcon = passwordIcon,
                password2Icon = password2Icon,
                onEmailChange = { email = it },
                onUsuarioChange = { usuario = it },
                onCarreraChange = { carrera = it },
                onPasswordChange = { password = it },
                onPassword2Change = { password2 = it },
                onPasswordVisibleChange = { passwordVisible = !passwordVisible },
                onPassword2VisibleChange = { passwordVisible2 = !passwordVisible2 }
            )
            if (password.isNotEmpty() && password != password2) {
                Text(stringResource(R.string.las_contrase_as_no_coinciden), color = Color.Red)
            }
            Spacer(modifier = Modifier.padding(15.dp))
            BotonesRegistro(
                onRegisterClick =  {(Log.d("Boton", "Registrando Usuario")) },
                onAlreadyAccountClick = {Log.d("Boton", "Ya tengo una cuenta")},
                onBackClick = {Log.d("Boton", "Volver")}
            )
        }
    }

}

@Composable
@Preview
fun RegisterScreenPreview() {
    RegisterScreen()
}

@Composable
fun FormularioRegistro(
    email: String,
    usuario: String,
    carrera: String,
    password: String,
    password2: String,
    passwordVisible: Boolean,
    passwordVisible2: Boolean,
    passwordIcon: Int,
    password2Icon: Int,
    onEmailChange: (String) -> Unit,
    onUsuarioChange: (String) -> Unit,
    onCarreraChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onPassword2Change: (String) -> Unit,
    onPasswordVisibleChange: () -> Unit,
    onPassword2VisibleChange: () -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
    ) {
        TextFieldApp(
            stringResource(R.string.email),
            value = email,
            onValueChange = onEmailChange
        )
        TextFieldApp(
            stringResource(R.string.usuario),
            value = usuario,
            onValueChange = onUsuarioChange
        )
        TextFieldApp(
            stringResource(R.string.carrera),
            value = carrera,
            onValueChange = onCarreraChange
        )
        TextFieldContraApp(
            stringResource(R.string.contrase_a),
            value = password,
            mostrarPassword = passwordVisible,
            click = onPasswordVisibleChange,
            onValueChange = onPasswordChange,
            icono = passwordIcon
        )
        TextFieldContraApp(
            stringResource(R.string.repetir_contrase_a),
            value = password2,
            mostrarPassword = passwordVisible2,
            click = onPassword2VisibleChange,
            onValueChange = onPassword2Change,
            icono = password2Icon
        )
    }


}

@Composable
@Preview(showBackground = true)
fun FormularioRegistroPreview() {
    FormularioRegistro(
        email = "",
        usuario = "",
        carrera = "",
        password = "",
        password2 = "",
        passwordVisible = false,
        passwordVisible2 = false,
        passwordIcon = R.drawable.ocultar,
        password2Icon = R.drawable.ocultar,
        onEmailChange = {},
        onUsuarioChange = {},
        onCarreraChange = {},
        onPasswordChange = {},
        onPassword2Change = {},
        onPasswordVisibleChange = {},
        onPassword2VisibleChange = {}
    )
}


@Composable
fun Header(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        LogoApp()
        Spacer(modifier = Modifier.padding(10.dp))
        Text(
            stringResource(R.string.registrarse),
            color = colorResource(R.color.verdetp2),
            fontFamily = BebasNeue,
            fontSize = 25.sp
        )
    }
}

@Composable
@Preview(showBackground = true)
fun HeaderPreview() {
    Header()
}


@Composable
fun BotonesRegistro(
    modifier: Modifier = Modifier,
    onRegisterClick: () -> Unit,
    onAlreadyAccountClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        AppButton(
            stringResource(R.string.registrarse), onClick = onRegisterClick
        )
        AppTextButton(stringResource(R.string.ya_tengo_una_cuenta), onClick = onAlreadyAccountClick)
        AppTextButton(stringResource(R.string.volver), onClick = onBackClick)
    }

}

@Composable
@Preview(showBackground = true)
fun BotonesRegistroPreview() {
    BotonesRegistro(onRegisterClick = {}, onAlreadyAccountClick = {}, onBackClick = {})
}
