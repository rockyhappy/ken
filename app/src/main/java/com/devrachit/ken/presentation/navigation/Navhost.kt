package com.devrachit.ken.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.devrachit.ken.presentation.screens.dashboard.compare.CompareScreen
import com.devrachit.ken.presentation.screens.dashboard.compare.CompareViewModel
import com.devrachit.ken.presentation.screens.dashboard.compareusers.CompareUsersScreen
import com.devrachit.ken.presentation.screens.dashboard.compareusers.CompareUsersViewModel
import com.devrachit.ken.presentation.screens.dashboard.home.HomeScreen
import com.devrachit.ken.presentation.screens.dashboard.home.HomeViewmodel
import com.devrachit.ken.presentation.screens.dashboard.questions.QuestionsScreen
import com.devrachit.ken.presentation.screens.dashboard.sheets.SheetsScreen
import com.devrachit.ken.presentation.screens.dashboard.userdetails.UserDetailsScreen


private const val ANIMATION_DURATION = 300

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    appNavController: NavHostController? = null
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        // Use extension function for each screen to avoid repetition
        animatedComposable(Screen.Home.route) {
            val viewmodel = hiltViewModel<HomeViewmodel>()
            HomeScreen(
                uiState = viewmodel.uiState.collectAsStateWithLifecycle().value,
                onFirstLoad = { viewmodel.loadUserDetails() },
            )
        }

        animatedComposable(Screen.Questions.route) {
            QuestionsScreen()
        }

        animatedComposable(Screen.Compare.route) {
            val viewmodel = hiltViewModel<CompareViewModel>()
            CompareScreen(
                uiState = viewmodel.userStatesValues.collectAsStateWithLifecycle().value,
                loadingStates = viewmodel.loadingStatesValues.collectAsStateWithLifecycle().value,
                friendsViewMode = viewmodel.friendsViewMode.collectAsStateWithLifecycle().value,
                onFirstLoad = { viewmodel.loadAllUsersInfo() },
                onSearchTextChange = { query -> viewmodel.updateSearchQuery(query) },
                onSuggestionClick = { username, userInfo ->
                    viewmodel.selectSearchResult(
                        username,
                        userInfo
                    )
                },
                onNavigateToUserDetails = { username ->
                    appNavController?.navigate(Screen.UserDetails.createRoute(username))
                },
                onNavigateToCompareUsers = { username ->
                    appNavController?.navigate(Screen.CompareUsers.createRoute(username))
                },
                onPlatformSearch = {
                    val currentQuery = viewmodel.userStatesValues.value.searchQuery
                    viewmodel.searchPlatformUser(currentQuery)
                },
                onHidePlatformResult = {
                    viewmodel.hidePlatformResult()
                },
                onRemoveUser = { username -> viewmodel.deleteUser(username) },
                onRefreshUser = { username -> viewmodel.refreshSingleUser(username) },
                onViewModeChanged = { viewMode -> viewmodel.updateFriendsViewMode(viewMode) },
                getEasyGraphData = { viewmodel.getEasyQuestionGraphData() },
                getMediumGraphData = { viewmodel.getMediumQuestionGraphData() },
                getHardGraphData = { viewmodel.getHardQuestionGraphData() }
            )
        }

        animatedComposable(Screen.Sheets.route) {
            SheetsScreen()
        }
        animatedComposable(
            route = Screen.CompareUsers.routeWithArgs,
            arguments = listOf(
                navArgument("username1") {
                    type = NavType.StringType
                    defaultValue = null
                    nullable = true
                },
                navArgument("username2") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                },
                navArgument("call_from_mainScreen") {
                    type = NavType.BoolType
                    defaultValue = true
                }
            )
        ) { backStackEntry ->
            val compareUsersViewModel: CompareUsersViewModel = hiltViewModel()
            val uiState = compareUsersViewModel.uiState.collectAsStateWithLifecycle()

            val username1 = backStackEntry.arguments?.getString("username1")
            val username2 = backStackEntry.arguments?.getString("username2")
            val callFromMainScreen = backStackEntry.arguments?.getBoolean("call_from_mainScreen") ?: true
            val availableUsers = remember {
                emptyList<String>()
            }

            LaunchedEffect(Unit) {
                compareUsersViewModel.loadAvailableUsers()
            }

            CompareUsersScreen(
                uiState = uiState.value,
                onInitialize = { u1, u2, users ->
                    compareUsersViewModel.initializeComparison(u1, u2, users)
                },
                onUser1Selected = { username ->
                    compareUsersViewModel.selectUser1(username)
                },
                onUser2Selected = { username ->
                    compareUsersViewModel.selectUser2(username)
                },
                onBackPress = { navController.popBackStack() },
                username1 = username1,
                username2 = username2,
                availableUsers = uiState.value.availableUsers,
                callFromMainScreen = callFromMainScreen
            )
        }
        // UserDetails route removed as it's now handled at the app level
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

fun navigateToTab(navController: NavController, route: String) {
    val currentRoute = navController.currentBackStackEntry?.destination?.route
    if (currentRoute != route) {
        navController.navigate(route) {
            popUpTo(navController.graph.startDestinationId) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}
