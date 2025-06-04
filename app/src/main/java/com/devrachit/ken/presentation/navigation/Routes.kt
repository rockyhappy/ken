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
    object CompareUsers : Screen("compare_users/{username1}?username2={username2}") {
        fun createRoute(username1: String, username2: String? = null) = 
            "compare_users/$username1" + if (username2 != null) "?username2=$username2" else ""
        const val routeWithArgs = "compare_users/{username1}?username2={username2}"
    }
}
