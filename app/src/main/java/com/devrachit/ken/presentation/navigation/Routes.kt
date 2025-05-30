package com.devrachit.ken.presentation.navigation

sealed class Screen(val route: String) {
    object Dashboard : Screen("dashboard")
    object Home : Screen("home")
    object Questions : Screen("questions")
    object Compare : Screen("compare")
    object Sheets : Screen("sheets")
    object Logout : Screen("logout")
    object UserDetails : Screen("user_details/{username}") {
        fun createRoute(username: String) = "user_details/$username"
        const val routeWithArgs = "user_details/{username}"
    }
}
