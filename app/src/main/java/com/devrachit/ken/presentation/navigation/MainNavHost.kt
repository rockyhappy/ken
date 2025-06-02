package com.devrachit.ken.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.devrachit.ken.presentation.screens.dashboard.ActivityContent.DashboardContent
import com.devrachit.ken.presentation.screens.dashboard.ActivityContent.MainViewModel
import com.devrachit.ken.presentation.screens.dashboard.ActivityContent.States
import com.devrachit.ken.presentation.screens.dashboard.userdetails.UserDetailsScreen
import com.devrachit.ken.presentation.screens.dashboard.userdetails.UserDetailsViewModel

private const val MAIN_ANIMATION_DURATION = 300

@Composable
fun MainNavHost(
    navController: NavHostController,
    viewModel: MainViewModel,
    username: String,
    uiStates: States
) {
    NavHost(navController = navController, startDestination = Screen.Dashboard.route) {
        mainAnimatedComposable(route = Screen.Dashboard.route) {
            DashboardContent(
                logout = viewModel::logout, 
                username = username, 
                uiState = uiStates,
                appNavController = navController
            )
        }
        
        mainAnimatedComposable(
            route = Screen.UserDetails.routeWithArgs,
            arguments = listOf(navArgument("username") { type = NavType.StringType })
        ) {
            val userDetailsViewModel: UserDetailsViewModel = hiltViewModel()
            val uiState = userDetailsViewModel.uiState.collectAsStateWithLifecycle()
            var hasInitiallyLoaded = rememberSaveable { mutableStateOf(false) }

            LaunchedEffect(true) {
                if (!hasInitiallyLoaded.value) {
                    userDetailsViewModel.loadUserDetails()
                    hasInitiallyLoaded.value = true
                }
            }

            UserDetailsScreen(
                uiState = uiState.value,
                onRefresh = { userDetailsViewModel.loadUserDetails() },
                onBackPress = { navController.popBackStack() },
                onDeleteUser = { username -> 
                    userDetailsViewModel.deleteUser(username)
                    navController.popBackStack()
                }
            )
        }
    }
}

/**
 * Extension function for NavGraphBuilder that adds a composable with standard
 * slide animations for main navigation
 */
private fun NavGraphBuilder.mainAnimatedComposable(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    content: @Composable (NavBackStackEntry) -> Unit
) {
    composable(
        route = route,
        arguments = arguments,
        enterTransition = { mainSlideEnterTransition(this) },
        exitTransition = { mainSlideExitTransition(this) },
        popEnterTransition = { mainSlidePopEnterTransition() },
        popExitTransition = { mainSlidePopExitTransition() }
    ) { backStackEntry ->
        content(backStackEntry)
    }
}

/**
 * Creates a slide-in enter transition for main navigation
 */
private fun mainSlideEnterTransition(
    scope: AnimatedContentTransitionScope<NavBackStackEntry>
): EnterTransition {
    return scope.slideIntoContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Left,
        animationSpec = tween(MAIN_ANIMATION_DURATION)
    )
}

/**
 * Creates a slide-out exit transition for main navigation
 */
private fun mainSlideExitTransition(
    scope: AnimatedContentTransitionScope<NavBackStackEntry>
): ExitTransition {
    return scope.slideOutOfContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Left,
        animationSpec = tween(MAIN_ANIMATION_DURATION)
    )
}

/**
 * Creates a slide-in transition for popping the back stack in main navigation
 */
private fun AnimatedContentTransitionScope<NavBackStackEntry>.mainSlidePopEnterTransition(): EnterTransition {
    return slideIntoContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Right,
        animationSpec = tween(MAIN_ANIMATION_DURATION)
    )
}

/**
 * Creates a slide-out transition for popping the back stack in main navigation
 */
private fun AnimatedContentTransitionScope<NavBackStackEntry>.mainSlidePopExitTransition(): ExitTransition {
    return slideOutOfContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Right,
        animationSpec = tween(MAIN_ANIMATION_DURATION)
    )
}
