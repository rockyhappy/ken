package com.devrachit.ken.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.devrachit.ken.presentation.screens.dashboard.compare.CompareScreen
import com.devrachit.ken.presentation.screens.dashboard.home.HomeScreen
import com.devrachit.ken.presentation.screens.dashboard.questions.QuestionsScreen
import com.devrachit.ken.presentation.screens.dashboard.sheets.SheetsScreen

@Composable
fun NavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(route = Screen.Home.route) {
            HomeScreen()
        }
        composable(route = Screen.Sheets.route) {
            SheetsScreen()
        }
        composable(route = Screen.Compare.route) {
            CompareScreen()
        }
        composable(route = Screen.Questions.route) {
            QuestionsScreen()
        }
    }
}