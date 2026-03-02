package com.example.tuprofe

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.tuprofe.navegation.AppNavegation
import com.example.tuprofe.ui.utils.BackButtonHeader
import com.example.tuprofe.ui.utils.TitleHeader
import androidx.compose.material3.*
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.tuprofe.navegation.NavigationLogic
import com.example.tuprofe.navegation.TuProfeBottomBar
import com.example.tuprofe.ui.utils.BackgroundImage
import com.example.tuprofe.ui.theme.Montserrat


@Composable
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun TuProfeApp(
){
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

@Preview()
@Composable
fun TuProfeAppPreview(){
    TuProfeApp(
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TuProfeTopBar(
    texto: String = "TuProfe",
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
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
            modifier = Modifier.fillMaxWidth(),
            style = TextStyle(
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                )
            )
        )

        HorizontalDivider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        )
    }
}

@Preview
@Composable
fun TuProfeTopBarPreview(){
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

@Composable
fun BottomBar(
    on1Click: () -> Unit,
    on2Click: () -> Unit,
    on3Click: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(50),
        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.verdetp))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                onClick = on1Click,
                modifier = Modifier.weight(1f)
            ) {
                Text("Incio", color = Color.White, fontSize = 16.sp)
            }
            VerticalDivider(
                modifier = Modifier.height(30.dp),
                thickness = 1.dp,
                color = Color.White
            )
            TextButton(
                onClick = on2Click,
                modifier = Modifier.weight(1f)
            ) {
                Text("Profesores", color = Color.White, fontSize = 16.sp)
            }
            VerticalDivider(
                modifier = Modifier.height(30.dp),
                thickness = 1.dp,
                color = Color.White
            )
            TextButton(
                onClick = on3Click,
                modifier = Modifier.weight(1f)
            ) {
                Text("Perfil", color = Color.White, fontSize = 16.sp)
            }
        }
    }
}
