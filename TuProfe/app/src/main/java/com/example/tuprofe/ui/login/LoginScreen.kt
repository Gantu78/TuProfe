package com.example.tuprofe.ui.login

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tuprofe.R
import com.example.tuprofe.ui.utils.*
import androidx.compose.animation.AnimatedVisibility
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    loginViewModel: LoginViewModel,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by loginViewModel.uiState.collectAsState()
    val icono = if (state.passwordVisible) R.drawable.mostrar else R.drawable.ocultar

    // Section entrance flags
    var showLogo   by remember { mutableStateOf(false) }
    var showForm   by remember { mutableStateOf(false) }
    var showButtons by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        showLogo = true
        delay(180)
        showForm = true
        delay(140)
        showButtons = true
    }

    Box(modifier = modifier.fillMaxSize()) {
        BackgroundImage()
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            // Logo — spring bounce scale + fade
            AnimatedVisibility(
                visible = showLogo,
                enter = fadeIn(tween(500)) +
                        slideInVertically(tween(500, easing = FastOutSlowInEasing)) { it / 3 }
            ) {
                LogoApp()
            }

            Spacer(modifier = Modifier.padding(30.dp))

            // Form — slight delay so it trails the logo
            AnimatedVisibility(
                visible = showForm,
                enter = fadeIn(tween(420)) +
                        slideInVertically(tween(420, easing = FastOutSlowInEasing)) { it / 4 }
            ) {
                FormularioInicio(
                    email = state.email,
                    password = state.password,
                    passwordVisible = state.passwordVisible,
                    icono = icono,
                    onEmailChange = { loginViewModel.setEmail(it) },
                    onPasswordChange = { loginViewModel.setPassword(it) },
                    onPasswordVisibleChange = { loginViewModel.togglePasswordVisibility() }
                )
            }

            // Error text with fade
            AnimatedVisibility(
                visible = state.mostrarMensajeError,
                enter = fadeIn(tween(260))
            ) {
                Text(
                    text = state.errorMessage,
                    color = Color.Red,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.padding(15.dp))

            // Buttons — last to appear
            AnimatedVisibility(
                visible = showButtons,
                enter = fadeIn(tween(380)) +
                        slideInVertically(tween(380, easing = FastOutSlowInEasing)) { it / 5 }
            ) {
                Botones(
                    onLoginClick = onLoginClick,
                    onForgotPasswordClick = onForgotPasswordClick,
                    onRegisterClick = onRegisterClick
                )
            }
        }
    }
}

@Composable
@Preview
fun HomeScreenPreview() {
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
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onPasswordVisibleChange: () -> Unit
) {
    Column(modifier = modifier) {
        TextFieldApp(
            modifier = Modifier,
            texto = stringResource(R.string.email),
            value = email,
            onValueChange = onEmailChange
        )
        TextFieldContraApp(
            modifier = Modifier,
            texto = stringResource(R.string.contrase_a),
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
fun FormularioInicioPreview() {
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
    modifier: Modifier = Modifier,
    onLoginClick: () -> Unit = {},
    onForgotPasswordClick: () -> Unit = {},
    onRegisterClick: () -> Unit = {},
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        AppButton(
            textoBoton = stringResource(R.string.iniciar_sesion),
            onClick = onLoginClick,
            modifier = Modifier.pressScaleEffect()
        )
        Spacer(modifier = Modifier.padding(30.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            AppButtonRow(
                stringResource(R.string.olvide_la_contrase_a),
                onClick = onForgotPasswordClick,
                modifier = Modifier.pressScaleEffect()
            )
            Spacer(modifier = Modifier.width(30.dp))
            AppButtonRow(
                stringResource(R.string.crear_cuenta),
                onClick = onRegisterClick,
                modifier = Modifier.pressScaleEffect()
            )
            Spacer(modifier = Modifier.width(16.dp))
        }
    }
}

@Composable
@Preview(showBackground = true)
fun BotonesPreview() {
    Botones()
}
