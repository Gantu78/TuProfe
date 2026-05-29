package com.example.tuprofe.ui.login

import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tuprofe.R
import com.example.tuprofe.ui.utils.*
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    loginViewModel: LoginViewModel,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    modifier: Modifier = Modifier.testTag("loginScreen")
) {
    val state by loginViewModel.uiState.collectAsState()
    val icono = if (state.passwordVisible) R.drawable.mostrar else R.drawable.ocultar
    val context = LocalContext.current
    val activity = context as Activity

    var showLogo    by rememberSaveable { mutableStateOf(false) }
    var showForm    by rememberSaveable { mutableStateOf(false) }
    var showButtons by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (!showButtons) {
            showLogo = true
            delay(180)
            showForm = true
            delay(140)
            showButtons = true
        }
    }

    // Cuando la pantalla vuelve al frente, cancela cualquier OAuth abandonado
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                loginViewModel.resetSocialLoadingIfPending()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    Box(modifier = modifier.fillMaxSize()) {
        BackgroundImage()
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            AnimatedVisibility(
                visible = showLogo,
                enter = fadeIn(tween(500)) +
                        slideInVertically(tween(500, easing = FastOutSlowInEasing)) { it / 3 }
            ) {
                LogoApp()
            }

            Spacer(modifier = Modifier.height(30.dp))

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

            AnimatedVisibility(
                visible = state.mostrarMensajeError,
                enter = fadeIn(tween(260))
            ) {
                Text(
                    text = state.errorMessage?.let { stringResource(it) } ?: "",
                    color = Color.Red,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, start = 16.dp, end = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            AnimatedVisibility(
                visible = showButtons,
                enter = fadeIn(tween(380)) +
                        slideInVertically(tween(380, easing = FastOutSlowInEasing)) { it / 5 }
            ) {
                Botones(
                    onLoginClick = onLoginClick,
                    onForgotPasswordClick = onForgotPasswordClick,
                    onRegisterClick = onRegisterClick,
                    onGoogleClick = { loginViewModel.signInWithGoogle(activity) },
                    onGitHubClick = { loginViewModel.signInWithGitHub(activity) },
                    isGoogleLoading = state.isGoogleLoading,
                    isGitHubLoading = state.isGitHubLoading
                )
            }
        }
    }
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
            texto = stringResource(R.string.email),
            value = email,
            onValueChange = onEmailChange
        )
        TextFieldContraApp(
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
fun Botones(
    modifier: Modifier = Modifier,
    onLoginClick: () -> Unit = {},
    onForgotPasswordClick: () -> Unit = {},
    onRegisterClick: () -> Unit = {},
    onGoogleClick: () -> Unit = {},
    onGitHubClick: () -> Unit = {},
    isGoogleLoading: Boolean = false,
    isGitHubLoading: Boolean = false,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(horizontal = 24.dp)
    ) {
        // ── Botón principal de login ───────────────────────────────────────────
        AppButton(
            textoBoton = stringResource(R.string.iniciar_sesion),
            onClick = onLoginClick,
            modifier = Modifier
                .fillMaxWidth()
                .pressScaleEffect()
                .testTag("login_button")
        )

        // ── Divisor "o continúa con" ───────────────────────────────────────────
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            HorizontalDivider(modifier = Modifier.weight(1f), color = Color.Gray.copy(alpha = 0.4f))
            Text(
                text = "  o continúa con  ",
                fontSize = 13.sp,
                color = Color.Gray
            )
            HorizontalDivider(modifier = Modifier.weight(1f), color = Color.Gray.copy(alpha = 0.4f))
        }

        // ── Botones sociales lado a lado ──────────────────────────────────────
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            // Google
            SocialLoginButton(
                label = "Google",
                iconRes = R.drawable.ic_google,
                isLoading = isGoogleLoading,
                onClick = onGoogleClick,
                containerColor = Color.White,
                contentColor = Color(0xFF3C4043),
                borderColor = Color(0xFFDDDDDD),
                modifier = Modifier.weight(1f)
            )

            // GitHub
            SocialLoginButton(
                label = "GitHub",
                iconRes = R.drawable.ic_github,
                isLoading = isGitHubLoading,
                onClick = onGitHubClick,
                containerColor = Color(0xFF24292E),
                contentColor = Color.White,
                borderColor = Color(0xFF444D56),
                modifier = Modifier.weight(1f)
            )
        }

        // ── Links secundarios ────────────────────────────────────────────────
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
            Spacer(modifier = Modifier.width(16.dp))
            AppButtonRow(
                stringResource(R.string.crear_cuenta),
                onClick = onRegisterClick,
                modifier = Modifier
                    .pressScaleEffect()
                    .testTag("register_button")
            )
        }
    }
}

// ── Componente reutilizable para botón social ─────────────────────────────────

@Composable
fun SocialLoginButton(
    label: String,
    iconRes: Int,
    isLoading: Boolean,
    onClick: () -> Unit,
    containerColor: Color,
    contentColor: Color,
    borderColor: Color,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        enabled = !isLoading,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, borderColor),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = containerColor.copy(alpha = 0.7f)
        ),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        modifier = modifier
            .height(48.dp)
            .pressScaleEffect()
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = contentColor,
                strokeWidth = 2.dp
            )
        } else {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(iconRes),
                    contentDescription = label,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = label,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = contentColor
                )
            }
        }
    }
}

// ── Previews ──────────────────────────────────────────────────────────────────

@Preview(showBackground = true)
@Composable
fun BotonesPreview() {
    Botones()
}

@Preview(showBackground = true)
@Composable
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
