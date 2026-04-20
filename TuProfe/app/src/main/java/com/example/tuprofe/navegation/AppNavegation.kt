package com.example.tuprofe.navegation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.tuprofe.R
import com.example.tuprofe.ui.config.ConfigScreen
import com.example.tuprofe.ui.config.ConfigViewModel
import com.example.tuprofe.ui.configPerfil.ConfigPerfilScreen
import com.example.tuprofe.ui.configPerfil.ConfigPerfilViewModel
import com.example.tuprofe.ui.detalle.DetalleScreen
import com.example.tuprofe.ui.detalle.DetalleViewModel
import com.example.tuprofe.ui.historia.HistorialScreen
import com.example.tuprofe.ui.historia.HistorialViewModel
import com.example.tuprofe.ui.LoadingScreen
import com.example.tuprofe.ui.login.HomeScreen
import com.example.tuprofe.ui.login.LoginViewModel
import com.example.tuprofe.ui.main.MainScreen
import com.example.tuprofe.ui.main.MainViewModel
import com.example.tuprofe.ui.perfilAjeno.UserProfileScreen
import com.example.tuprofe.ui.perfilAjeno.UserProfileViewModel
import com.example.tuprofe.ui.profe.ProfeScreen
import com.example.tuprofe.ui.profe.ProfeViewModel
import com.example.tuprofe.ui.register.RegisterScreen
import com.example.tuprofe.ui.register.RegisterViewModel
import com.example.tuprofe.ui.resetPassword.ResetPasswordScreen
import com.example.tuprofe.ui.resetPassword.ResetPasswordViewModel
import com.example.tuprofe.ui.review.create.CreateReviewScreen
import com.example.tuprofe.ui.review.create.CreateReviewViewModel
import com.example.tuprofe.ui.review.edit.EditReviewScreen
import com.example.tuprofe.ui.review.edit.EditReviewViewModel
import com.example.tuprofe.ui.search.SearchScreen
import com.example.tuprofe.ui.splash.SplashScreen


sealed class Screen(val route: String){
    object Splash : Screen("Splash")
    object Login : Screen("Login")
    object Register : Screen("Register")
    object PasswordReset : Screen("PasswordReset")
    object Main : Screen("Main")
    object Search: Screen("Search")
    object Profe : Screen("profe/{profeId}") {
        fun createRoute(profeId: String) = "profe/$profeId"
    }
    object Historial : Screen("Historial")
    object Loading : Screen("Loading")
    object CreateReview : Screen("CreateReview")
    object EditReview : Screen("EditReview/{reviewId}") {
        fun createRoute(reviewId: String) = "EditReview/$reviewId"
    }
    object ConfigPerfil : Screen("ConfigPerfil")
    object Configuracion : Screen("Configuracion")
    object Detalle : Screen("Detalle/{reviewId}"){
        fun createRoute(reviewId: String) = "Detalle/$reviewId"
    }
    
    object Profile : Screen("Profile/{userId}") {
        fun createRoute(userId: String) = "Profile/$userId"
    }

}


@Composable
fun AppNavegation(
    navController: NavHostController,
    modifier: Modifier = Modifier
){
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        modifier = modifier
    ){
        composable(route = Screen.Splash.route){
            SplashScreen(navigateToLogin = {
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Splash.route) {
                        inclusive = true
                    }
                }
                }, navigateToMain = {
                navController.navigate(Screen.Main.route) {
                    popUpTo(Screen.Splash.route) {
                        inclusive = true
                    }
                }
            })

        }

        composable(route = Screen.Login.route){
            val loginViewModel: LoginViewModel = hiltViewModel()
            val loginState by loginViewModel.uiState.collectAsState()

            androidx.compose.runtime.LaunchedEffect(loginState.navigate) {
                if (loginState.navigate) {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            }

            HomeScreen(
                loginViewModel = loginViewModel,
                onLoginClick = { loginViewModel.loginClick() },
                onRegisterClick = {
                    navController.navigate(Screen.Register.route)
                },
                onForgotPasswordClick = {
                    navController.navigate(Screen.PasswordReset.route)
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
            val registerViewModel: RegisterViewModel = hiltViewModel()
            val registerState by registerViewModel.uiState.collectAsState()

            androidx.compose.runtime.LaunchedEffect(registerState.navigateHome) {
                if (registerState.navigateHome) {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            }

            RegisterScreen(
                registerViewModel = registerViewModel,
                onRegisterClick = {
                    registerViewModel.onRegisterClickSecure()
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        composable(route = Screen.PasswordReset.route){
            val resetPasswordViewModel: ResetPasswordViewModel = hiltViewModel()
            ResetPasswordScreen(
                resetPasswordViewModel = resetPasswordViewModel,
                onVolverClick = {navController.popBackStack()}
            )
        }
        composable(route = Screen.Main.route){
            val mainViewModel: MainViewModel = hiltViewModel()
            MainScreen(
                mainViewModel = mainViewModel,
                onResenaClick = { reviewId ->
                    navController.navigate(Screen.Detalle.createRoute(reviewId))
                },
                onProfileClick = { profesor ->
                    navController.navigate(Screen.Profe.createRoute(profesor.profeId))
                },
                onUserClick = { userId ->
                    navController.navigate(Screen.Profile.createRoute(userId))
                }
            )
        }
        composable(
            route = Screen.Profe.route,
            arguments = listOf(navArgument("profeId"){ type = NavType.StringType })
            
        ){
            val profeViewModel: ProfeViewModel = hiltViewModel()
                ProfeScreen(
                    profeViewModel = profeViewModel,
                    onResenaClick = { reviewId ->
                        navController.navigate(Screen.Detalle.createRoute(reviewId))
                    },
                    onProfileClick = { p ->
                        navController.navigate(Screen.Profe.createRoute(p.profeId))
                    },
                    onUserClick = { userId ->
                        navController.navigate(Screen.Profile.createRoute(userId))
                    }
                )
            }
        composable(route = Screen.Historial.route){
            val historialViewModel: HistorialViewModel = hiltViewModel()

            HistorialScreen(
                historialViewModel = historialViewModel,
                onVerCalificacionClick = { review ->
                    navController.navigate(Screen.Detalle.createRoute(review.reviewId))
                },
                onProfessorClick = { profeId ->
                    navController.navigate(Screen.Profe.createRoute(profeId))
                },
                onEditClick = { reviewId ->
                    navController.navigate(Screen.EditReview.createRoute(reviewId))
                }
            )
        }

        composable(route = Screen.CreateReview.route){
            val createReviewViewModel: CreateReviewViewModel = hiltViewModel()
            CreateReviewScreen(
                viewModel = createReviewViewModel,
                onSuccess = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screen.EditReview.route,
            arguments = listOf(navArgument("reviewId") { type = NavType.StringType })
        ) {
            val editReviewViewModel: EditReviewViewModel = hiltViewModel()
            EditReviewScreen(
                viewModel = editReviewViewModel,
                onSuccess = {
                    navController.popBackStack()
                },
            )
        }


        composable(route = Screen.ConfigPerfil.route){
            val configPerfilViewModel: ConfigPerfilViewModel = hiltViewModel()
            ConfigPerfilScreen(
                configPerfilViewModel = configPerfilViewModel,
                onSaveSuccess = {
                    navController.popBackStack()
                },
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(route = Screen.Loading.route){
            LoadingScreen()
        }
        composable(route = Screen.Configuracion.route){
            val configViewModel: ConfigViewModel = hiltViewModel()

            ConfigScreen(
                configViewModel = configViewModel,
                onEditProfileClick = {
                    navController.navigate(Screen.ConfigPerfil.route)
                },
                onLogoutClick = {
                    configViewModel.onLogoutClick()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onCalifClick = {
                    navController.navigate(Screen.Historial.route)
                }

            )
        }
        composable(
            route = Screen.Detalle.route,
            arguments = listOf(navArgument("reviewId"){type = NavType.StringType})
        ){
            val detalleViewModel: DetalleViewModel = hiltViewModel()

            DetalleScreen(
                reviewId = navController.currentBackStackEntry?.arguments?.getString("reviewId") ?: "",
                detalleViewModel = detalleViewModel,
                onProfileClick = { profesor ->
                    navController.navigate(Screen.Profe.createRoute(profesor.profeId))
                } ,
                onUserClick = { userId ->
                    navController.navigate(Screen.Profile.createRoute(userId))
                }
            )

        }

        composable(
            route = Screen.Profile.route,
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) {
            val userProfileViewModel: UserProfileViewModel = hiltViewModel()
            UserProfileScreen(
                viewModel = userProfileViewModel,
                onProfessorClick = { profeId ->
                    navController.navigate(Screen.Profe.createRoute(profeId))
                }
            )
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
    BottomNavItem(filledIcon = Icons.Filled.Add, outLinedIcon = Icons.Outlined.Add, route = Screen.CreateReview.route),
    BottomNavItem(filledIcon = Icons.Filled.Notifications, outLinedIcon = Icons.Outlined.Notifications, route = Screen.Loading.route),
    BottomNavItem(filledIcon = Icons.Filled.Person, outLinedIcon = Icons.Outlined.Person, route = Screen.Configuracion.route)
)

@Composable
fun TuProfeBottomBar(
    navController: NavHostController,
    items: List<BottomNavItem> = bottomNavItems
) {

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

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
                                    popUpTo(navController.graph.findStartDestination().id) {
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
                    navController.navigate(middleItem.route) {
                        launchSingleTop = true
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
