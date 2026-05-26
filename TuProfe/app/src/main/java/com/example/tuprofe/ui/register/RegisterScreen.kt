package com.example.tuprofe.ui.register


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
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
    registerViewModel: RegisterViewModel,
    onRegisterClick: () -> Unit,
    onBackClick: () -> Unit,
    onSuccessDismiss: () -> Unit = { registerViewModel.onSuccessDialogDismissed() }
) {
    val state by registerViewModel.uiState.collectAsState()

    val passwordIcon = if (state.passwordVisible) R.drawable.mostrar else R.drawable.ocultar
    val password2Icon = if (state.passwordVisible) R.drawable.mostrar else R.drawable.ocultar

    Box(modifier = Modifier.fillMaxSize()) {
        BackgroundImage()

        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Header()

            if (state.mostrarMensajeError) {
                Text(
                    text = state.errorMessage,
                    color = Color.Red,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, start = 16.dp, end = 16.dp)
                )
            }

            if (state.mostrarMensaje) {
                AlertDialog(
                    onDismissRequest = onSuccessDismiss,
                    title = {
                        Text(
                            text = "¡Cuenta creada!",
                            color = colorResource(id = R.color.verdetp)
                        )
                    },
                    text = {
                        Text(text = "Por favor, verifica tu correo para activar tu cuenta.")
                    },
                    confirmButton = {
                        TextButton(onClick = onSuccessDismiss) {
                            Text(
                                text = "Aceptar",
                                color = colorResource(id = R.color.verdetp)
                            )
                        }
                    }
                )
            }

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

            BotonesRegistro(
                isLoading = state.isLoading,
                onRegisterClick = onRegisterClick,
                onBackClick = onBackClick
            )
        }

        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = colorResource(id = R.color.verdetp)
                )
            }
        }
    }
}

@Composable
@Preview
fun RegisterScreenPreview() {
    RegisterScreen(
        registerViewModel = viewModel(),
        onRegisterClick = {},
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
            modifier = Modifier.testTag("txtEmail"),
            stringResource(R.string.email),
            value = email,
            onValueChange = onEmailChange
        )
        TextFieldApp(
            modifier = Modifier.testTag("txtUsuario"),
            stringResource(R.string.usuario),
            value = usuario,
            onValueChange = onUsuarioChange
        )
        TextFieldApp(
            modifier = Modifier.testTag("txtCarrera"),
            stringResource(R.string.carrera),
            value = carrera,
            onValueChange = onCarreraChange
        )
        TextFieldContraApp(
            modifier = Modifier.testTag("txtContraseña"),
            stringResource(R.string.contrase_a),
            value = password,
            mostrarPassword = passwordVisible,
            click = onPasswordVisibleChange,
            onValueChange = onPasswordChange,
            icono = passwordIcon
        )
        TextFieldContraApp(
            modifier = Modifier.testTag("txtContraseña2"),
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
    isLoading: Boolean = false,
    onRegisterClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        AppButton(
            stringResource(R.string.registrarse),
            onClick = onRegisterClick,
            enabled = !isLoading,
            modifier = Modifier.testTag("registerbutton")
        )
        AppTextButton(
            modifier = Modifier.testTag("volver_button"),
            stringResource(R.string.volver),
            onClick = onBackClick)
    }

}

@Composable
@Preview(showBackground = true)
fun BotonesRegistroPreview() {
    BotonesRegistro(onRegisterClick = {}, onBackClick = {})
}