package com.devrachit.ken.presentation.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.devrachit.ken.presentation.screens.dashboard.compare.CompareScreen
import com.devrachit.ken.presentation.screens.dashboard.home.HomeScreen
import com.devrachit.ken.presentation.screens.dashboard.questions.QuestionsScreen
import com.devrachit.ken.presentation.screens.dashboard.sheets.SheetsScreen

// Animation duration to match slide_in_right.xml (600ms)
private const val ANIMATION_DURATION = 400

@Composable
fun NavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        // Use extension function for each screen to avoid repetition
        animatedComposable(Screen.Home.route) {
            HomeScreen()
        }
        
        animatedComposable(Screen.Questions.route) {
            QuestionsScreen()
        }
        
        animatedComposable(Screen.Compare.route) {
            CompareScreen()
        }
        
        animatedComposable(Screen.Sheets.route) {
            SheetsScreen()
        }
    }
}

/**
 * Extension function for NavGraphBuilder that adds a composable with standard
 * pager-like slide animations based on navigation direction
 */
private fun NavGraphBuilder.animatedComposable(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    content: @Composable (NavBackStackEntry) -> Unit
) {
    composable(
        route = route,
        arguments = arguments,
        enterTransition = { slideEnterTransition(this) },
        exitTransition = { slideExitTransition(this) },
        popEnterTransition = { slidePopEnterTransition() },
        popExitTransition = { slidePopExitTransition() }
    ) { backStackEntry ->
        // Call the content function with the NavBackStackEntry
        content(backStackEntry)
    }
}

/**
 * Creates a slide-in enter transition based on the navigation direction
 */
private fun slideEnterTransition(
    scope: AnimatedContentTransitionScope<NavBackStackEntry>
): EnterTransition {
    val targetIndex = getRouteIndex(scope.targetState.destination.route) ?: 0
    val initialIndex = getRouteIndex(scope.initialState.destination.route) ?: 0
    
    val direction = if (targetIndex > initialIndex)
        AnimatedContentTransitionScope.SlideDirection.Left
    else
        AnimatedContentTransitionScope.SlideDirection.Right
    
    return scope.slideIntoContainer(
        towards = direction,
        animationSpec = tween(ANIMATION_DURATION)
    )
}

/**
 * Creates a slide-out exit transition based on the navigation direction
 */
private fun slideExitTransition(
    scope: AnimatedContentTransitionScope<NavBackStackEntry>
): ExitTransition {
    val targetIndex = getRouteIndex(scope.targetState.destination.route) ?: 0
    val initialIndex = getRouteIndex(scope.initialState.destination.route) ?: 0
    
    val direction = if (targetIndex > initialIndex)
        AnimatedContentTransitionScope.SlideDirection.Left
    else
        AnimatedContentTransitionScope.SlideDirection.Right
    
    return scope.slideOutOfContainer(
        towards = direction,
        animationSpec = tween(ANIMATION_DURATION)
    )
}

/**
 * Creates a slide-in transition for popping the back stack (going back)
 */
private fun AnimatedContentTransitionScope<NavBackStackEntry>.slidePopEnterTransition(): EnterTransition {
    return slideIntoContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Right,
        animationSpec = tween(ANIMATION_DURATION)
    )
}

/**
 * Creates a slide-out transition for popping the back stack (going back)
 */
private fun AnimatedContentTransitionScope<NavBackStackEntry>.slidePopExitTransition(): ExitTransition {
    return slideOutOfContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Right,
        animationSpec = tween(ANIMATION_DURATION)
    )
}