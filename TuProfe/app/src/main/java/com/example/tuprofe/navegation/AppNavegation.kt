package com.example.tuprofe.navegation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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

@Composable
fun AppNavegation(
    navController: NavHostController,
    modifier: Modifier = Modifier
){
    NavHost(
        navController = navController,
        startDestination = "start",
        modifier = modifier
    ){
        composable(route = "start"){
            HomeScreen(
                onLoginClick = {
                    navController.navigate("Main"){
                        popUpTo(0){
                            inclusive = true
                        }
                    }
                },
                onRegisterClick = {
                    navController.navigate("Register"){
                        popUpTo(0){
                            inclusive = true
                        }
                    }
                },
                onForgotPasswordClick = {
                    navController.navigate("PasswordReset"){
                        popUpTo(0){
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable(route = "Register"){
            RegisterScreen(
                    onRegisterClick = {navController.navigate("start"){
                        popUpTo(0){
                            inclusive = true
                        }
                    }},
                    onAlreadyAccountClick = {navController.navigate("start"){
                        popUpTo(0){
                            inclusive = true
                        }
                    }},
                    onBackClick = {navController.navigate("start"){
                        popUpTo(0){
                            inclusive = true
                        }
                    }}
            )
        }
        composable(route = "PasswordReset"){
            ResetPasswordScreen(
                onVolverClick = {navController.navigate("start"){
                    popUpTo(0){
                        inclusive = true
                    }
                }}
            )
        }
        composable(route = "Main"){
            MainScreen(
                onResenaClick = { reviewId ->
                    navController.navigate("Detalle/$reviewId")
                }
            )
        }
        composable(route = "Profe"){
            ProfeScreen()
        }
        composable(route = "Profile"){
            ConfigScreen()
        }
        composable(route = "Loading"){
            LoadingScreen()
        }
        composable(route = "Configuracion"){
            ConfigScreen()
        }
        composable(
            route = "Detalle/{ReviewId}",
            arguments = listOf(navArgument("ReviewId"){type = NavType.IntType})
        ){
            val ReviewId = it.arguments?.getInt("ReviewId")?: 0

            val review = LocalReview.Reviews.find { it.reviewId == ReviewId }

            if(review != null){
                DetalleScreen(
                    onLike = {},
                    onComment = {},
                    onShare = {},
                    ReviewInfo = review,
                    responseReviews = LocalReview.Reviews,
                    modifier = Modifier.padding(12.dp)
                )

            } else (Text("Reseña no encontrada"))

        }
    }

}