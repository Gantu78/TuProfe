package com.example.tuprofe.ui.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tuprofe.ui.utils.BackgroundImage
import com.example.tuprofe.ui.utils.LogoApp

@Composable
fun SplashScreen(
    navigateToLogin: () -> Unit,
    navigateToMain: () -> Unit,
    modifier: Modifier = Modifier,
    splashViewModel: SplashViewModel = hiltViewModel()
) {
    val state by splashViewModel.uiState.collectAsState()

    LaunchedEffect(state.navigateToMain) {
        if (state.navigateToMain) {
            navigateToMain()
            splashViewModel.onNavigationHandled()
        }
    }

    LaunchedEffect(state.navigateToLogin) {
        if (state.navigateToLogin) {
            navigateToLogin()
            splashViewModel.onNavigationHandled()
        }
    }

    SplashContent(modifier = modifier)
}

@Composable
fun SplashContent(modifier: Modifier = Modifier) {
    // Trigger animation on first composition
    var animationStarted by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { animationStarted = true }

    // Spring bounce for scale — gives the logo a lively entrance
    val scale by animateFloatAsState(
        targetValue = if (animationStarted) 1f else 0.72f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "logoScale"
    )
    // Fade in slightly faster than the scale so it feels snappy
    val alpha by animateFloatAsState(
        targetValue = if (animationStarted) 1f else 0f,
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing),
        label = "logoAlpha"
    )

    Box(modifier = modifier.fillMaxSize()) {
        BackgroundImage()
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    this.alpha = alpha
                },
            contentAlignment = Alignment.Center
        ) {
            LogoApp()
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SplashScreenPreview() {
    SplashContent()
}
