package com.example.tuprofe.navegation

import androidx.compose.animation.fadeIn
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
import com.example.tuprofe.ui.comment.detalle.CommentDetalleScreen
import com.example.tuprofe.ui.comment.detalle.CommentDetalleViewModel
import com.example.tuprofe.ui.comment.edit.EditCommentScreen
import com.example.tuprofe.ui.comment.edit.EditCommentViewModel
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
import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween


// ── Reusable transition specs ─────────────────────────────────────────────────

private fun slideEnter() = slideInHorizontally(
    tween(300, easing = FastOutSlowInEasing)
) { it / 3 } + fadeIn(tween(260))


private fun slideExit() =
    slideOutHorizontally(tween(240)) { -it / 8 } + fadeOut(tween(200))

private fun slidePopEnter() = slideInHorizontally(
    tween(300, easing = FastOutSlowInEasing)
) { -it / 3 } + fadeIn(tween(260))


private fun slidePopExit() =
    slideOutHorizontally(tween(240)) { it / 4 } + fadeOut(tween(200))


private fun tabEnter() = fadeIn(tween(280))
private fun tabExit() = fadeOut(tween(200))


private fun modalEnter() =
    slideInVertically(tween(360, easing = FastOutSlowInEasing)) { it } +
            fadeIn(tween(300))


private fun modalExit() =
    slideOutVertically(tween(280)) { it } + fadeOut(tween(240))

// ── Screens ───────────────────────────────────────────────────────────────────



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

    object CommentDetalle : Screen("CommentDetalle/{commentId}") {
        fun createRoute(commentId: String) = "CommentDetalle/$commentId"
    }

    object EditComment : Screen("EditComment/{commentId}") {
        fun createRoute(commentId: String) = "EditComment/$commentId"
    }

}


@Composable
fun AppNavegation(
    navController: NavHostController,
    modifier: Modifier = Modifier
){
    NavHost(navController = navController,
        startDestination = Screen.Splash.route,
        modifier = modifier,
        enterTransition = { slideEnter() },
        exitTransition = { slideExit() },
        popEnterTransition = { slidePopEnter() },
        popExitTransition = { slidePopExit() }
    ){
        // ── Splash ────────────────────────────────────────────────────────────
        composable(
            route = Screen.Splash.route,
            enterTransition = { fadeIn(tween(500)) },
            exitTransition = { fadeOut(tween(400)) }
        ) {
            SplashScreen(navigateToLogin = {
                navController.navigate(Screen.Login.route)
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Splash.route) {
                        inclusive = true
                    }
                }
            },
                navigateToMain = { navController.navigate(Screen.Main.route) {
                    popUpTo(Screen.Splash.route) {
                        inclusive = true
                    }
                } }
            )
        }

        composable(
            route = Screen.Login.route,
            enterTransition = { tabEnter() },
            exitTransition = { tabExit() },
            popEnterTransition = { tabEnter() },
            popExitTransition = { tabExit() }
        ){
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
        composable(
            route = Screen.Register.route,
            enterTransition = { tabEnter() },
            exitTransition = { tabExit() },
            popEnterTransition = { tabEnter() },
            popExitTransition = { tabExit() }
        ){
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
        composable(
            route = Screen.PasswordReset.route,
            enterTransition = { tabEnter() },
            exitTransition = { tabExit() },
            popEnterTransition = { tabEnter() },
            popExitTransition = { tabExit() }
        ){
            val resetPasswordViewModel: ResetPasswordViewModel = hiltViewModel()
            ResetPasswordScreen(
                resetPasswordViewModel = resetPasswordViewModel,
                onVolverClick = {navController.popBackStack()}
            )
        }
        composable(
            route = Screen.Main.route,
            enterTransition = { tabEnter() },
            exitTransition = { tabExit() },
            popEnterTransition = { tabEnter() },
            popExitTransition = { tabExit() }
        ){
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
        composable(
            route = Screen.Historial.route,
            enterTransition = { tabEnter() },
            exitTransition = { tabExit() },
            popEnterTransition = { tabEnter() },
            popExitTransition = { tabExit() }){
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
                },
                onVerComentarioClick = { commentId ->
                    navController.navigate(Screen.CommentDetalle.createRoute(commentId))
                },
                onEditCommentClick = { commentId ->
                    navController.navigate(Screen.EditComment.createRoute(commentId))
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


        composable(
            route = Screen.ConfigPerfil.route,
            enterTransition = { tabEnter() },
            exitTransition = { tabExit() },
            popEnterTransition = { tabEnter() },
            popExitTransition = { tabExit() }
        ){
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
                },
                onUserClick = { userId ->
                    navController.navigate(Screen.Profile.createRoute(userId))
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
                },
                onUserClick = { userId ->
                    navController.navigate(Screen.Profile.createRoute(userId))
                },
                onCommentClick = { commentId ->
                    navController.navigate(Screen.CommentDetalle.createRoute(commentId))
                }
            )
        }

        composable(
            route = Screen.CommentDetalle.route,
            arguments = listOf(navArgument("commentId") { type = NavType.StringType })
        ) {
            val commentDetalleViewModel: CommentDetalleViewModel = hiltViewModel()
            CommentDetalleScreen(
                commentId = navController.currentBackStackEntry?.arguments?.getString("commentId") ?: "",
                viewModel = commentDetalleViewModel,
                onCommentClick = { commentId ->
                    navController.navigate(Screen.CommentDetalle.createRoute(commentId))
                },
                onUserClick = { userId ->
                    navController.navigate(Screen.Profile.createRoute(userId))
                }
            )
        }

        composable(
            route = Screen.EditComment.route,
            arguments = listOf(navArgument("commentId") { type = NavType.StringType })
        ) {
            val editCommentViewModel: EditCommentViewModel = hiltViewModel()
            EditCommentScreen(
                viewModel = editCommentViewModel,
                onSuccess = { navController.popBackStack() }
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
                },
                onUserClick = { userId ->
                    navController.navigate(Screen.Profile.createRoute(userId))
                },
                onReviewClick = { reviewId ->
                    navController.navigate(Screen.Detalle.createRoute(reviewId))
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
