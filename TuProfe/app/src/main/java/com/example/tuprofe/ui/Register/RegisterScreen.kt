package com.example.tuprofe.ui.Register


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tuprofe.R
import com.example.tuprofe.ui.theme.BebasNeue
import com.example.tuprofe.ui.utils.AppButton
import com.example.tuprofe.ui.utils.AppTextButton
import com.example.tuprofe.ui.utils.BackgroundImage
import com.example.tuprofe.ui.utils.LogoApp
import com.example.tuprofe.ui.utils.TextFieldApp
import com.example.tuprofe.ui.utils.TextFieldContraApp
import kotlinx.coroutines.delay

@Composable
fun RegisterScreen(
    registerViewModel: RegisterViewModel,
    modifier: Modifier = Modifier,
    onRegisterClick: () -> Unit,
    onAlreadyAccountClick: () -> Unit,
    onBackClick: () -> Unit,
) {

    val state by registerViewModel.uiState.collectAsState()

    val passwordIcon = if (state.passwordVisible) R.drawable.mostrar else R.drawable.ocultar
    val password2Icon = if (state.passwordVisible) R.drawable.mostrar else R.drawable.ocultar

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
                email = state.email,
                usuario = state.usuario,
                carrera = state.carrera,
                password = state.password1,
                password2 = state.password2,
                passwordVisible = state.passwordVisible,
                passwordIcon = passwordIcon,
                password2Icon = password2Icon,
                onEmailChange = { registerViewModel.setEmail(it) },
                onUsuarioChange = { registerViewModel.setUsuario(it) },
                onCarreraChange = { registerViewModel.setCarrera(it) },
                onPasswordChange = { registerViewModel.setPassword1(it) },
                onPassword2Change = { registerViewModel.setPassword2(it) },
                onPasswordVisibleChange = { registerViewModel.togglePasswordVisibility() }

            )
            if (state.mostrarMensajeError) {
                Text(state.errorMessage, color = Color.Red, modifier = Modifier.padding(8.dp))
            } else if(state.mostrarMensaje){
                Text(
                    stringResource(R.string.su_cuenta_ha_sido_creada_con_exito), 
                    color = Color.Green,
                    modifier = Modifier.padding(8.dp)
                )
            }

            Spacer(modifier = Modifier.padding(15.dp))
            BotonesRegistro(
                onRegisterClick =  onRegisterClick,
                onAlreadyAccountClick = onAlreadyAccountClick,
                onBackClick = onBackClick
            )
        }
    }

}

@Composable
@Preview
fun RegisterScreenPreview() {
    RegisterScreen(
        registerViewModel = viewModel(),
        onRegisterClick = {},
        onAlreadyAccountClick = {},
        onBackClick = {}
    )
}

@Composable
fun FormularioRegistro(
    email: String,
    usuario: String,
    carrera: String,
    password: String,
    password2: String,
    passwordVisible: Boolean,
    passwordIcon: Int,
    password2Icon: Int,
    onEmailChange: (String) -> Unit,
    onUsuarioChange: (String) -> Unit,
    onCarreraChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onPassword2Change: (String) -> Unit,
    onPasswordVisibleChange: () -> Unit,
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
            mostrarPassword = passwordVisible,
            click = onPasswordVisibleChange,
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
        passwordIcon = R.drawable.ocultar,
        password2Icon = R.drawable.ocultar,
        onEmailChange = {},
        onUsuarioChange = {},
        onCarreraChange = {},
        onPasswordChange = {},
        onPassword2Change = {},
        onPasswordVisibleChange = {},
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
