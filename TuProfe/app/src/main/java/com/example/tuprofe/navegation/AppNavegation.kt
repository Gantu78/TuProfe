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
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Message
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
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
import com.example.tuprofe.data.local.LocalProfesor
import com.example.tuprofe.data.local.LocalReview
import com.example.tuprofe.ui.ConfigPerfil.ConfigPerfilScreen
import com.example.tuprofe.ui.Config.ConfigScreen
import com.example.tuprofe.ui.Config.ConfigViewModel
import com.example.tuprofe.ui.ConfigPerfil.ConfigPerfilViewModel
import com.example.tuprofe.ui.Detalle.DetalleScreen
import com.example.tuprofe.ui.Detalle.DetalleViewModel
import com.example.tuprofe.ui.Historia.HistorialViewModel
import com.example.tuprofe.ui.HistorialScreen
import com.example.tuprofe.ui.Login.HomeScreen
import com.example.tuprofe.ui.LoadingScreen
import com.example.tuprofe.ui.Login.LoginViewModel
import com.example.tuprofe.ui.Main.MainScreen
import com.example.tuprofe.ui.Main.MainViewModel
import com.example.tuprofe.ui.Profe.ProfeScreen
import com.example.tuprofe.ui.Profe.ProfeViewModel
import com.example.tuprofe.ui.Register.RegisterScreen
import com.example.tuprofe.ui.Register.RegisterViewModel
import com.example.tuprofe.ui.ResetPassword.ResetPasswordScreen
import com.example.tuprofe.ui.ResetPassword.ResetPasswordViewModel
import com.example.tuprofe.ui.Search.SearchScreen


sealed class Screen(val route: String){
    object Login : Screen("Login")
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
        startDestination = Screen.Login.route,
        modifier = modifier
    ){
        composable(route = Screen.Login.route){
            val loginViewModel: LoginViewModel = viewModel()
            HomeScreen(
                loginViewModel = loginViewModel,
                onLoginClick = {
                    navController.navigate(Screen.Main.route){
                        popUpTo(0){
                            inclusive = true
                        }
                    }
                },
                onRegisterClick = {
                    navController.navigate(Screen.Register.route){
                        popUpTo(0){
                            inclusive = true
                        }
                    }
                },
                onForgotPasswordClick = {
                    navController.navigate(Screen.PasswordReset.route){
                        popUpTo(0){
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(route = Screen.Search.route){
            SearchScreen(
                onProfessorClick = { profeId ->
                    navController.navigate(Screen.Profe.createRoute(profeId))
                }
            )
        }
        composable(route = Screen.Register.route){
            val registerViewModel: RegisterViewModel = viewModel()
            RegisterScreen(
                registerViewModel = registerViewModel,
                onRegisterClick = {
                    if(registerViewModel.onRegisterClickSecure()){
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                }},
                onAlreadyAccountClick = {navController.navigate(Screen.Login.route){
                    popUpTo(0){
                        inclusive = true
                    }
                }},
                onBackClick = {navController.navigate(Screen.Login.route){
                    popUpTo(0){
                        inclusive = true
                    }
                }}
            )
        }
        composable(route = Screen.PasswordReset.route){
            val resetPasswordViewModel: ResetPasswordViewModel = viewModel()
            ResetPasswordScreen(
                resetPasswordViewModel = resetPasswordViewModel,
                onVolverClick = {navController.navigate(Screen.Login.route){
                    popUpTo(0){
                        inclusive = true
                    }
                }}
            )
        }
        composable(route = Screen.Main.route){
            val mainViewModel: MainViewModel = viewModel()
            MainScreen(
                mainViewModel = mainViewModel,
                onResenaClick = { reviewId ->
                    navController.navigate(Screen.Detalle.createRoute(reviewId))
                },
                onProfileClick = { profesor ->
                    navController.navigate(Screen.Profe.createRoute(profesor.profeId))
                }
            )
        }
        composable(
            route = Screen.Profe.route,
            arguments = listOf(navArgument("profeId"){ type = NavType.IntType })
        ){
            val profeId = it.arguments?.getInt("profeId") ?: 0
            val profesor = LocalProfesor.profesores.find { it.profeId == profeId }

            if(profesor != null){

                val profeViewModel: ProfeViewModel = viewModel()
                ProfeScreen(
                    profeViewModel = profeViewModel,
                    profesor = profesor,
                    onResenaClick = { reviewId ->
                        navController.navigate(Screen.Detalle.createRoute(reviewId))
                    },
                    onProfileClick = { profesor ->
                        navController.navigate(Screen.Profe.createRoute(profesor.profeId))
                    }
                )

            } else {
                Text("Profesor no encontrado")
            }
        }
        composable(route = Screen.Historial.route){
            val historialViewModel: HistorialViewModel = viewModel()

            HistorialScreen(
                historialViewModel = historialViewModel,
                onVerCalificacionClick = { review ->
                    navController.navigate(Screen.Detalle.createRoute(review.reviewId))
                }
            )
        }

        composable(route = Screen.ConfigPerfil.route){
            val configPerfilViewModel: ConfigPerfilViewModel = viewModel()
            ConfigPerfilScreen(
                configPerfilViewModel = configPerfilViewModel,
                onChangePassword = {
                    navController.navigate(Screen.PasswordReset.route)
                },
                onGuardarCambiosClick = {
                    navController.navigate(Screen.Configuracion.route) {
                        popUpTo(0){
                            inclusive = true
                        }
                    }
                },
                onBorrarCuentaClick = {
                    navController.navigate(Screen.Login.route)
                }
            )
        }

        composable(route = Screen.Loading.route){
            LoadingScreen()
        }
        composable(route = Screen.Configuracion.route){
            val configViewModel: ConfigViewModel = viewModel()

            ConfigScreen(
                configViewModel = configViewModel,
                onProfileClick = {
                    navController.navigate(Screen.ConfigPerfil.route)
                },
                onLogoutClick = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0){
                            inclusive = true
                        }
                    }
                },
                onCalifClick = {
                    navController.navigate(Screen.Historial.route){
                        popUpTo(0){
                            inclusive = true
                        }
                    }
                }

            )
        }
        composable(
            route = Screen.Detalle.route,
            arguments = listOf(navArgument("reviewId"){type = NavType.IntType})
        ){
            val reviewId = it.arguments?.getInt("reviewId")?: 0
            val review = LocalReview.Reviews.find { it.reviewId == reviewId }
            val detalleViewModel: DetalleViewModel = viewModel()

            if(review != null){

                DetalleScreen(
                    detalleViewModel = detalleViewModel,
                    onProfileClick = { profesor ->
                        navController.navigate(Screen.Profe.createRoute(profesor.profeId))
                    }
                )

            } else (Text(stringResource(R.string.rese_a_no_encontrada)))

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