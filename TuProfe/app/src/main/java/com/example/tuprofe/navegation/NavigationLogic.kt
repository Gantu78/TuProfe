package com.example.tuprofe.navegation

object NavigationLogic {

    private val NoTopBar = listOf(
        Screen.Login.route,
        Screen.PasswordReset.route,
        Screen.Register.route,
        Screen.Splash.route
    )

    private val NoBottomBar = listOf(
        Screen.Login.route,
        Screen.PasswordReset.route,
        Screen.Splash.route,
    )

    fun ShouldShowTopBar(route: String?) = !NoTopBar.contains(route)


    fun ShouldShowBottomBar(route: String?) = !NoBottomBar.contains(route)


}