package com.example.tuprofe.ui.Splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
fun SplashContent(
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        BackgroundImage()
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            LogoApp()
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SplashScreenPreview() {
    SplashContent()
}
