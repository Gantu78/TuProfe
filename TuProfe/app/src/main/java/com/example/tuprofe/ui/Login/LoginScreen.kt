package com.example.tuprofe.ui.Login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tuprofe.R
import com.example.tuprofe.ui.utils.AppButton
import com.example.tuprofe.ui.utils.AppButtonRow
import com.example.tuprofe.ui.utils.BackgroundImage
import com.example.tuprofe.ui.utils.TextFieldApp
import com.example.tuprofe.ui.utils.TextFieldContraApp
import com.example.tuprofe.ui.utils.LogoApp

@Composable
fun HomeScreen(
    loginViewModel: LoginViewModel,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    modifier: Modifier = Modifier
){

    val state by loginViewModel.uiState.collectAsState()

    val icono = if (state.passwordVisible) R.drawable.mostrar else R.drawable.ocultar
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        BackgroundImage()
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            LogoApp()
            Spacer(modifier = Modifier.padding(30.dp))
            FormularioInicio(
                email = state.email,
                password = state.password,
                passwordVisible = state.passwordVisible,
                icono = icono,
                onEmailChange = { loginViewModel.setEmail(it) },
                onPasswordChange = { loginViewModel.setPassword(it) },
                onPasswordVisibleChange = { loginViewModel.togglePasswordVisibility() }
            )

            if (state.mostrarMensajeError) {
                Text(text = state.errorMessage, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
            }

            Spacer(modifier = Modifier.padding(15.dp))
            Botones(
                onLoginClick =  onLoginClick ,
                onForgotPasswordClick = onForgotPasswordClick ,
                onRegisterClick =  onRegisterClick
            )
        }
    }
}

@Composable
@Preview
fun HomeScreenPreview(){
    HomeScreen(
        loginViewModel = viewModel(),
        onLoginClick = {},
        onRegisterClick = {},
        onForgotPasswordClick = {}
    )
}

@Composable
fun FormularioInicio(
    modifier: Modifier = Modifier,
    email: String,
    password: String,
    passwordVisible: Boolean,
    icono: Int,
    onEmailChange : (String) -> Unit,
    onPasswordChange : (String) -> Unit,
    onPasswordVisibleChange : () -> Unit

){


    Column(
        modifier = modifier
    ) {
        TextFieldApp(
            stringResource(R.string.email),
            value = email,
            onValueChange = onEmailChange
        )
        TextFieldContraApp(
            stringResource(R.string.contrase_a),
            value = password,
            mostrarPassword = passwordVisible,
            click = onPasswordVisibleChange,
            onValueChange = onPasswordChange,
            icono = icono
        )
    }

}

@Composable
@Preview(showBackground = true)
fun FormularioInicioPreview(){
    FormularioInicio(
        email = "",
        password = "",
        passwordVisible = false,
        icono = R.drawable.ocultar,
        onEmailChange = {},
        onPasswordChange = {},
        onPasswordVisibleChange = {}
    )
}


@Composable
fun Botones(
    onLoginClick: () -> Unit = {},
    onForgotPasswordClick: () -> Unit = {},
    onRegisterClick: () -> Unit = {},
    modifier: Modifier = Modifier
){
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        AppButton(
            stringResource(R.string.iniciar_sesion),
             onLoginClick
        )
        Spacer(modifier = Modifier.padding(30.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {

            AppButtonRow(stringResource(R.string.olvide_la_contrase_a), onClick = onForgotPasswordClick)
            Spacer(modifier = Modifier.width(30.dp))
            AppButtonRow(stringResource(R.string.crear_cuenta), onClick = onRegisterClick)
            Spacer(modifier = Modifier.width(16.dp))
        }
    }
}

@Composable
@Preview(showBackground = true)
fun BotonesPreview(){
    Botones()
}