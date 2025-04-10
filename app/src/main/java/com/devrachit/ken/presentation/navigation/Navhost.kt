package com.devrachit.ken.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.devrachit.ken.presentation.screens.dashboard.compare.CompareScreen
import com.devrachit.ken.presentation.screens.dashboard.home.HomeScreen
import com.devrachit.ken.presentation.screens.dashboard.questions.QuestionsScreen
import com.devrachit.ken.presentation.screens.dashboard.sheets.SheetsScreen


private const val ANIMATION_DURATION = 400

@Composable
fun NavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(
            route = Screen.Home.route,
            enterTransition = { 
                val targetIndex = getRouteIndex(targetState.destination.route) ?: 0
                val initialIndex = getRouteIndex(initialState.destination.route) ?: 0
                if (targetIndex > initialIndex) {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(ANIMATION_DURATION)
                    )
                } else {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(ANIMATION_DURATION)
                    )
                }
            },
            exitTransition = { 
                val targetIndex = getRouteIndex(targetState.destination.route) ?: 0
                val initialIndex = getRouteIndex(initialState.destination.route) ?: 0
                if (targetIndex > initialIndex) {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(ANIMATION_DURATION)
                    )
                } else {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(ANIMATION_DURATION)
                    )
                }
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(ANIMATION_DURATION)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(ANIMATION_DURATION)
                )
            }
        ) {
            HomeScreen()
        }
        
        composable(
            route = Screen.Questions.route,
            enterTransition = { 
                val targetIndex = getRouteIndex(targetState.destination.route) ?: 0
                val initialIndex = getRouteIndex(initialState.destination.route) ?: 0
                if (targetIndex > initialIndex) {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(ANIMATION_DURATION)
                    )
                } else {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(ANIMATION_DURATION)
                    )
                }
            },
            exitTransition = { 
                val targetIndex = getRouteIndex(targetState.destination.route) ?: 0
                val initialIndex = getRouteIndex(initialState.destination.route) ?: 0
                if (targetIndex > initialIndex) {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(ANIMATION_DURATION)
                    )
                } else {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(ANIMATION_DURATION)
                    )
                }
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(ANIMATION_DURATION)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(ANIMATION_DURATION)
                )
            }
        ) {
            QuestionsScreen()
        }
        
        composable(
            route = Screen.Compare.route,
            enterTransition = { 
                val targetIndex = getRouteIndex(targetState.destination.route) ?: 0
                val initialIndex = getRouteIndex(initialState.destination.route) ?: 0
                if (targetIndex > initialIndex) {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(ANIMATION_DURATION)
                    )
                } else {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(ANIMATION_DURATION)
                    )
                }
            },
            exitTransition = { 
                val targetIndex = getRouteIndex(targetState.destination.route) ?: 0
                val initialIndex = getRouteIndex(initialState.destination.route) ?: 0
                if (targetIndex > initialIndex) {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(ANIMATION_DURATION)
                    )
                } else {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(ANIMATION_DURATION)
                    )
                }
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(ANIMATION_DURATION)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(ANIMATION_DURATION)
                )
            }
        ) {
            CompareScreen()
        }
        
        composable(
            route = Screen.Sheets.route,
            enterTransition = { 
                val targetIndex = getRouteIndex(targetState.destination.route) ?: 0
                val initialIndex = getRouteIndex(initialState.destination.route) ?: 0
                if (targetIndex > initialIndex) {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(ANIMATION_DURATION)
                    )
                } else {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(ANIMATION_DURATION)
                    )
                }
            },
            exitTransition = { 
                val targetIndex = getRouteIndex(targetState.destination.route) ?: 0
                val initialIndex = getRouteIndex(initialState.destination.route) ?: 0
                if (targetIndex > initialIndex) {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(ANIMATION_DURATION)
                    )
                } else {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(ANIMATION_DURATION)
                    )
                }
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(ANIMATION_DURATION)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(ANIMATION_DURATION)
                )
            }
        ) {
            SheetsScreen()
        }
    }
}