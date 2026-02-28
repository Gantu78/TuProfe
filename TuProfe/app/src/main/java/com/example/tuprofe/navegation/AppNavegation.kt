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
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
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
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tuprofe.R
import com.example.tuprofe.data.local.LocalReview
import com.example.tuprofe.ui.ConfigPerfilScreen
import com.example.tuprofe.ui.ConfigScreen
import com.example.tuprofe.ui.DetalleScreen
import com.example.tuprofe.ui.HistorialScreen
import com.example.tuprofe.ui.HomeScreen
import com.example.tuprofe.ui.LoadingScreen
import com.example.tuprofe.ui.MainScreen
import com.example.tuprofe.ui.ProfeScreen
import com.example.tuprofe.ui.RegisterScreen
import com.example.tuprofe.ui.ResetPasswordScreen



sealed class Screen(val route: String){
    object Home : Screen("Home")
    object Register : Screen("Register")
    object PasswordReset : Screen("PasswordReset")
    object Main : Screen("Main")
    object Profe : Screen("Profe")
    object Profile : Screen("Profile")
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
            HomeScreen(
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
        composable(route = Screen.Register.route){
            RegisterScreen(
                    onRegisterClick = {navController.navigate(Screen.Home.route){
                        popUpTo(0){
                            inclusive = true
                        }
                    }},
                    onAlreadyAccountClick = {navController.navigate(Screen.Home.route){
                        popUpTo(0){
                            inclusive = true
                        }
                    }},
                    onBackClick = {navController.navigate(Screen.Home.route){
                        popUpTo(0){
                            inclusive = true
                        }
                    }}
            )
        }
        composable(route = Screen.PasswordReset.route){
            ResetPasswordScreen(
                onVolverClick = {navController.navigate(Screen.Home.route){
                    popUpTo(0){
                        inclusive = true
                    }
                }}
            )
        }
        composable(route = Screen.Main.route){
            MainScreen(
                onResenaClick = { reviewId ->
                    navController.navigate(Screen.Detalle.createRoute(reviewId))
                },
                onProfileClick = {
                    navController.navigate(Screen.Profe.route)
                }
            )
        }
        composable(route = Screen.Profe.route){
            ProfeScreen(
                onResenaClick = { reviewId ->
                    navController.navigate(Screen.Detalle.createRoute(reviewId))
                },
                onProfileClick = {
                    navController.navigate(Screen.Profile.route)
                }
            )
        }
        composable(route = Screen.Profile.route){
            HistorialScreen(
                onFilterClick = {},
                onVerCalificacionClick = { review ->
                    navController.navigate(Screen.Detalle.createRoute(review.reviewId))
                }
            )
        }

        composable(route = Screen.ConfigPerfil.route){
            ConfigPerfilScreen(
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
                    navController.navigate(Screen.Home.route)
                }
            )
        }

        composable(route = Screen.Loading.route){
            LoadingScreen()
        }
        composable(route = Screen.Configuracion.route){
            ConfigScreen(
                onProfileClick = {
                    navController.navigate(Screen.ConfigPerfil.route)
                },
                onLogoutClick = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(0){
                        inclusive = true
                    }
                    }
                },
                onCalifClick = {
                    navController.navigate(Screen.Profile.route)
                },
                onAyudaClick = {
                },
                onPrivacidadClick = {
                },
                onNotificacionesClick = {
                }



            )
        }
        composable(
            route = Screen.Detalle.route,
            arguments = listOf(navArgument("reviewId"){type = NavType.IntType})
        ){
            val reviewId = it.arguments?.getInt("reviewId")?: 0
            val review = LocalReview.Reviews.find { it.reviewId == reviewId }

            if(review != null){

                DetalleScreen(
                    onLike = {},
                    onComment = {},
                    onShare = {},
                    ReviewInfo = review,
                    responseReviews = LocalReview.Reviews,
                    onProfileClick = {
                        navController.navigate(Screen.Profe.route)
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
    BottomNavItem(filledIcon = Icons.Filled.Search, outLinedIcon = Icons.Outlined.Search, route = Screen.PasswordReset.route),
    BottomNavItem(filledIcon = Icons.Filled.Send, outLinedIcon = Icons.Outlined.Send, route = Screen.Profe.route),
    BottomNavItem(filledIcon = Icons.Filled.Person, outLinedIcon = Icons.Outlined.Person, route = Screen.Profile.route),
    BottomNavItem(filledIcon = Icons.Filled.Settings, outLinedIcon = Icons.Outlined.Settings, route = Screen.Configuracion.route)
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
