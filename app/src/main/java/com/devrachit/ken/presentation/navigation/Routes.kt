package com.devrachit.ken.presentation.navigation

sealed class Screen ( val route :String){
    object Home : Screen("home")
    object Questions : Screen("questions")
    object Compare : Screen("compare")
    object Sheets : Screen("sheets")
    object Logout : Screen("logout")
}