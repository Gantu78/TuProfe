package com.example.tuprofe

import android.annotation.SuppressLint
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.tuprofe.navegation.AppNavegation
import com.example.tuprofe.ui.MainScreen
import com.example.tuprofe.ui.utils.BackButtonHeader
import com.example.tuprofe.ui.utils.SearchBar
import com.example.tuprofe.ui.utils.TitleHeader
import androidx.compose.material3.*
import androidx.compose.material3.CheckboxDefaults.colors
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.tuprofe.navegation.NavigationLogic
import com.example.tuprofe.navegation.Screen
import com.example.tuprofe.navegation.TuProfeBottomBar
import com.example.tuprofe.ui.theme.BebasNeue
import com.example.tuprofe.ui.utils.BackgroundImage


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
            contentWindowInsets = WindowInsets(0),
            topBar = {
                if(NavigationLogic.ShouldShowTopBar(currentRoute)){
                    // Eliminamos el Surface que causaba el tinte grisáceo/transparente raro
                    TuProfeTopBar()
                }
            },
            bottomBar = {
                if(NavigationLogic.ShouldShowBottomBar(currentRoute)){
                    TuProfeBottomBar(navController = navController)
                }
            }
        ) { paddingValues ->
            AppNavegation(
                navController = navController,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues) // Aplicamos el padding para que no se solape
            )
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
){
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent,
            titleContentColor = colorResource(R.color.verdetp), // Color del título aquí
            navigationIconContentColor = colorResource(R.color.verdetp),
            actionIconContentColor = colorResource(R.color.verdetp)
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 40.dp),
        title = {
            Text(
                text = texto,
                fontWeight = FontWeight.Bold,
                fontFamily = BebasNeue,
                style = MaterialTheme.typography.titleLarge
                // Quitamos el color de aquí para que use el titleContentColor de la TopAppBar
            )
        }
    )
}

@Preview
@Composable
fun TuProfeTopBarPreview(){
    TuProfeTopBar()
}

@Composable
fun HeaderSection(
    title: String,
    showSearchBar: Boolean = true,
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

        if (showSearchBar) {
            SearchBar()
            Spacer(modifier = Modifier.height(20.dp))
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
