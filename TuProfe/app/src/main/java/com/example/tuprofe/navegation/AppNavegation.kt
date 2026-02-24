package com.example.tuprofe.navegation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.tuprofe.data.local.LocalReview
import com.example.tuprofe.ui.ConfigScreen
import com.example.tuprofe.ui.DetalleScreen
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
                }
            )
        }
        composable(route = Screen.Profe.route){
            ProfeScreen()
        }
        composable(route = Screen.Profile.route){
            ConfigScreen()
        }
        composable(route = Screen.Loading.route){
            LoadingScreen()
        }
        composable(route = Screen.Configuracion.route){
            ConfigScreen()
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

                )

            } else (Text("Reseña no encontrada"))

        }
    }

}
