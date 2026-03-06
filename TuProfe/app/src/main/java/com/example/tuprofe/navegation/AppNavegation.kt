package com.example.tuprofe.navegation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Message
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tuprofe.R
import com.example.tuprofe.ui.ConfigPerfil.ConfigPerfilScreen
import com.example.tuprofe.ui.Config.ConfigScreen
import com.example.tuprofe.ui.Detalle.DetalleScreen

import com.example.tuprofe.ui.Login.HomeScreen
import com.example.tuprofe.ui.LoadingScreen
import com.example.tuprofe.ui.Main.MainScreen
import com.example.tuprofe.ui.Profe.ProfeScreen
import com.example.tuprofe.ui.Register.RegisterScreen
import com.example.tuprofe.ui.Register.RegisterViewModel
import com.example.tuprofe.ui.ResetPassword.ResetPasswordScreen
import com.example.tuprofe.ui.Search.SearchScreen
import androidx.compose.runtime.getValue
import com.example.tuprofe.ui.Config.ConfigViewModel
import com.example.tuprofe.ui.ConfigPerfil.ConfigPerfilViewModel
import com.example.tuprofe.ui.Detalle.DetalleViewModel
import com.example.tuprofe.ui.Historia.HistorialViewModel
import com.example.tuprofe.ui.HistorialScreen
import com.example.tuprofe.ui.Login.LoginViewModel
import com.example.tuprofe.ui.Main.MainViewModel
import com.example.tuprofe.ui.Profe.ProfeViewModel
import com.example.tuprofe.ui.ResetPassword.ResetPasswordViewModel
import com.example.tuprofe.ui.Search.SearchViewModel


sealed class Screen(val route: String){
    object Home : Screen("Home")
    object Register : Screen("Register")
    object PasswordReset : Screen("PasswordReset")
    object Main : Screen("Main")
    object Search: Screen("Search")
    object Profe : Screen("profe/{profeId}") {
        fun createRoute(profeId: Int) = "profe/$profeId"
    }
    object Historial : Screen("Historial")
    object Loading : Screen("Loading")
    object ConfigPerfil : Screen("ConfigPerfil")
    object Configuracion : Screen("Configuracion")
    object Detalle : Screen("Detalle/{reviewId}"){
        fun createRoute(reviewId: Int) = "Detalle/$reviewId"
        }
    }


@Composable
fun AppNavegation(
    navController: NavHostController,
    modifier: Modifier = Modifier
){
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ){
        composable(route = Screen.Home.route){
            val loginViewModel: LoginViewModel = viewModel()
            val state by loginViewModel.uiState.collectAsState()

            if(state.navigate) {
                navController.navigate(Screen.Main.route) {
                    popUpTo(0) {
                        inclusive = true
                    }
                }
            }
                if (state.register) {
                    navController.navigate(Screen.Register.route) {
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                }

                if (state.forgotPassword) {
                    navController.navigate(Screen.PasswordReset.route) {
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                }

                HomeScreen(
                    loginViewModel = loginViewModel
                )
        }

        composable(route = Screen.Search.route){
            val searchViewModel: SearchViewModel = viewModel()
            val state by searchViewModel.uiState.collectAsState()

            LaunchedEffect(state.navigateToProfileId) {
                state.navigateToProfileId?.let { id ->
                    navController.navigate(Screen.Profe.createRoute(id))
                    searchViewModel.onNavigationHandled()
                }
            }

            SearchScreen(searchViewModel = searchViewModel)
        }
        composable(route = Screen.Register.route){
            val registerViewModel: RegisterViewModel = viewModel()
            val state by registerViewModel.uiState.collectAsState()
            if(state.navigateHome){
                navController.navigate(Screen.Home.route){
                    popUpTo(0){
                        inclusive = true
                    }
                }
            }

            RegisterScreen(
                    registerViewModel = registerViewModel

            )
        }
        composable(route = Screen.PasswordReset.route){

            val resetPasswordViewModel: ResetPasswordViewModel = viewModel()
            val state by resetPasswordViewModel.uiState.collectAsState()

            if(state.navigate){
                navController.navigate(Screen.Home.route){
                    popUpTo(0){
                        inclusive = true
                    }
                }
            }
            ResetPasswordScreen(
                resetPasswordViewModel = resetPasswordViewModel
            )
        }
        composable(route = Screen.Main.route){
            val mainViewModel: MainViewModel = viewModel()
            val state by mainViewModel.uiState.collectAsState()

            LaunchedEffect(state.navigateToReviewId) {
                state.navigateToReviewId?.let { id ->
                    navController.navigate(Screen.Detalle.createRoute(id))
                    mainViewModel.onNavigationHandled()
                }
            }

            LaunchedEffect(state.navigateToProfileId) {
                state.navigateToProfileId?.let { id ->
                    navController.navigate(Screen.Profe.createRoute(id))
                    mainViewModel.onNavigationHandled()
                }
            }

            MainScreen(mainViewModel = mainViewModel)
        }
        composable(
            route = Screen.Profe.route,
            arguments = listOf(navArgument("profeId"){ type = NavType.IntType })
        ){
            val profeViewModel: ProfeViewModel = viewModel()
            val state by profeViewModel.uiState.collectAsState()

            LaunchedEffect(state.navigateToReviewId) {
                state.navigateToReviewId?.let { id ->
                    navController.navigate(Screen.Detalle.createRoute(id))
                    profeViewModel.onNavigationHandled()
                }
            }

            LaunchedEffect(state.navigateToProfileId) {
                state.navigateToProfileId?.let { id ->
                    navController.navigate(Screen.Profe.createRoute(id))
                    profeViewModel.onNavigationHandled()
                }
            }

            ProfeScreen(profeViewModel = profeViewModel)
        }
        composable(route = Screen.Historial.route){
            val historialViewModel: HistorialViewModel = viewModel()
            val state by historialViewModel.uiState.collectAsState()

            LaunchedEffect(state.navigateToReviewId) {
                state.navigateToReviewId?.let { id ->
                    navController.navigate(Screen.Detalle.createRoute(id))
                    historialViewModel.onNavigationHandled()
                }
            }

            HistorialScreen(historialViewModel = historialViewModel)
        }

        composable(route = Screen.ConfigPerfil.route){

            val configPerfilViewModel: ConfigPerfilViewModel = viewModel()
            val state by configPerfilViewModel.uiState.collectAsState()

            if(state.navigate)navController.navigate(Screen.Configuracion.route){popUpTo(0){ inclusive = true }}

            ConfigPerfilScreen(
                configPerfilViewModel = configPerfilViewModel
            )
        }

        composable(route = Screen.Loading.route){
            LoadingScreen()
        }
        composable(route = Screen.Configuracion.route){

            val configViewModel: ConfigViewModel = viewModel()
            val state by configViewModel.uiState.collectAsState()

            if(state.Profile) navController.navigate(Screen.ConfigPerfil.route){popUpTo(0){ inclusive = true }}
            if(state.Logout) navController.navigate(Screen.Home.route){popUpTo(0){ inclusive = true }}
            if(state.Ayuda) navController.navigate(Screen.Loading.route){popUpTo(0){ inclusive = true }}
            if(state.calificacion) navController.navigate(Screen.Historial.route){popUpTo(0){ inclusive = true }}
            if(state.Notificaciones) navController.navigate(Screen.Loading.route){popUpTo(0){ inclusive = true }}
            if(state.Privacidad) navController.navigate(Screen.Loading.route){popUpTo(0){ inclusive = true }}

            ConfigScreen(
                configViewModel = configViewModel
            )
        }
        composable(
            route = Screen.Detalle.route,
            arguments = listOf(navArgument("reviewId"){type = NavType.IntType})
        ){
            val detalleViewModel: DetalleViewModel = viewModel()
            val state by detalleViewModel.uiState.collectAsState()

            LaunchedEffect(state.navigateToProfile) {
                state.navigateToProfile?.let { id ->
                    navController.navigate(Screen.Profe.createRoute(id))
                    detalleViewModel.onNavigationHandled()
                }
            }

            LaunchedEffect(state.navigateBack) {
                if (state.navigateBack) {
                    navController.popBackStack()
                    detalleViewModel.onNavigationHandled()
                }
            }

            DetalleScreen(detalleViewModel = detalleViewModel)
        }
    }

}

data class BottomNavItem(
    val filledIcon: ImageVector,
    val outLinedIcon: ImageVector,
    val route: String
)

val bottomNavItems = listOf(
    BottomNavItem(filledIcon = Icons.Filled.Home, outLinedIcon = Icons.Outlined.Home, route = Screen.Main.route),
    BottomNavItem(filledIcon = Icons.Filled.Search, outLinedIcon = Icons.Outlined.Search, route = Screen.Search.route),
    BottomNavItem(filledIcon = Icons.Filled.Add, outLinedIcon = Icons.Outlined.Add, route = Screen.Profe.route),
    BottomNavItem(filledIcon = Icons.Filled.Message, outLinedIcon = Icons.Outlined.Message, route = Screen.Loading.route),
    BottomNavItem(filledIcon = Icons.Filled.Person, outLinedIcon = Icons.Outlined.Person, route = Screen.Configuracion.route)
)

@Composable
fun TuProfeBottomBar(
    navController: NavHostController,
    items: List<BottomNavItem> = bottomNavItems
) {

    val currentRoute =
        navController.currentBackStackEntryAsState().value?.destination?.route

    Box {

        NavigationBar(
            containerColor = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp
        ) {

            items.forEachIndexed { index, item ->


                if (index == 2) {
                    Spacer(modifier = Modifier.weight(1f))
                } else {

                    val isSelected = currentRoute == item.route

                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            if (currentRoute != item.route) {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = if (isSelected)
                                    item.filledIcon
                                else
                                    item.outLinedIcon,
                                contentDescription = "",
                                tint = if (isSelected)
                                    colorResource(R.color.verdetp)
                                else
                                    Color.Gray
                            )
                        },
                        label = null,
                        alwaysShowLabel = false,
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = Color.Transparent
                        )
                    )
                }
            }
        }


        val middleItem = items[2]

        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-28).dp)
                .size(64.dp)
                .shadow(12.dp, CircleShape)
                .background(
                    color = colorResource(R.color.verdetp),
                    shape = CircleShape
                )
                .border(
                    width = 4.dp,
                    color = Color.White,
                    shape = CircleShape
                )
                .clickable {
                    if (currentRoute != middleItem.route) {
                        navController.navigate(middleItem.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = middleItem.filledIcon,
                contentDescription = "",
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomNavPreview(){
    TuProfeBottomBar(navController = rememberNavController())
}
