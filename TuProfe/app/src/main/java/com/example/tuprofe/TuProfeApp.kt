package com.example.tuprofe

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.tuprofe.navegation.AppNavegation
import com.example.tuprofe.navegation.NavigationLogic
import com.example.tuprofe.navegation.TuProfeBottomBar
import com.example.tuprofe.ui.utils.BackButtonHeader
import com.example.tuprofe.ui.utils.BackgroundImage
import com.example.tuprofe.ui.utils.TitleHeader
import com.example.tuprofe.ui.theme.Montserrat

@Composable
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun TuProfeApp() {
    val navController = rememberNavController()
    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry.value?.destination?.route

    Box(modifier = Modifier.fillMaxSize()) {
        BackgroundImage()
        Scaffold(
            containerColor = Color.Transparent,
            contentColor = Color.Unspecified,
            contentWindowInsets = WindowInsets(0),
            topBar = {
                if (NavigationLogic.ShouldShowTopBar(currentRoute)) {
                    TuProfeTopBar()
                }
            },
            bottomBar = {
                if (NavigationLogic.ShouldShowBottomBar(currentRoute)) {
                    TuProfeBottomBar(navController = navController)
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                AppNavegation(
                    navController = navController,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TuProfeTopBar(
    modifier: Modifier = Modifier,
    texto: String = "TuProfe",
) {
    // Surface provides a clean background; no hard shadow or divider
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 0.dp,
        tonalElevation = 0.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                text = texto,
                fontWeight = FontWeight.Bold,
                fontFamily = Montserrat,
                fontSize = 26.sp,
                color = colorResource(R.color.verdetp),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                style = TextStyle(
                    platformStyle = PlatformTextStyle(includeFontPadding = false)
                )
            )
            // Soft gradient accent line — blends with content instead of cutting it
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.5.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color.Transparent,
                                colorResource(R.color.verdetp).copy(alpha = 0.18f),
                                colorResource(R.color.verdetp).copy(alpha = 0.45f),
                                colorResource(R.color.verdetp).copy(alpha = 0.18f),
                                Color.Transparent
                            )
                        )
                    )
            )
        }
    }
}

@Preview
@Composable
fun TuProfeAppPreview() {
    TuProfeApp()
}

@Preview
@Composable
fun TuProfeTopBarPreview() {
    TuProfeTopBar()
}

@Composable
fun HeaderSection(
    title: String,
    modifier: Modifier = Modifier,
    onBackClick: (() -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (onBackClick != null) {
            BackButtonHeader(onBackClick = onBackClick)
        }
        TitleHeader(title = title)
        Spacer(modifier = Modifier.height(20.dp))
    }
}
