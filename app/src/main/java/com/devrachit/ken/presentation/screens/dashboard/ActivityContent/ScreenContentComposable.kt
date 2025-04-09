package com.devrachit.ken.presentation.screens.dashboard.ActivityContent

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.devrachit.ken.R
import com.devrachit.ken.presentation.navigation.NavGraph
import com.devrachit.ken.presentation.navigation.Screen
import com.devrachit.ken.presentation.screens.dashboard.Widgets.DashboardHeader
import com.devrachit.ken.presentation.screens.dashboard.Widgets.NavItem
import com.devrachit.ken.utility.composeUtility.sdp
import com.devrachit.ken.utility.composeUtility.shadowEffect
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.Job


@OptIn(ExperimentalPagerApi::class)
@Composable
fun ScreenContents(
    username: String,
    uiStates: States,
    modifier: Modifier = Modifier,
    onClick: () -> Job,
    drawerProgress: Float = 0f
) {
    // Create NavController that's shared with NavGraph
    val navController = rememberNavController()
    
    // Get current back stack entry
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    
    // Bottom row animation parameters
    val yOffset = lerp(0f, 100f, drawerProgress)
    val alpha = lerp(1f, 0f, drawerProgress)

    // Add spring animation for scale when row reappears
    val scale = animateFloatAsState(
        targetValue = if (drawerProgress < 0.5f) 1f else 0.8f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "bottom_row_scale_animation"
    )

    // Define navigation items with routes
    val navItems = remember {
        // Data class for navigation items
        data class NavItemData(
            val label: String,
            val outlinedIcon: Int,
            val filledIcon: Int,
            val route: String
        )

        // Map of tab index to NavItemData
        mapOf(
            0 to NavItemData("Home", 
                R.drawable.ic_home_outlined, 
                R.drawable.ic_home_filled,
                Screen.Home.route),
            1 to NavItemData("Questions", 
                R.drawable.ic_questions_outlined, 
                R.drawable.ic_questions_filled,
                Screen.Questions.route),
            2 to NavItemData("Compare", 
                R.drawable.ic_compare_outlined, 
                R.drawable.ic_compare_filled,
                Screen.Compare.route),
            3 to NavItemData("Sheets", 
                R.drawable.ic_sheets_outlined, 
                R.drawable.ic_sheets_filled,
                Screen.Sheets.route)
        )
    }

    // Determine the current selected route
    val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Home.route
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(0.dp)
            )
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .background(colorResource(R.color.bg_neutral))
                .fillMaxSize()
        ) {
            DashboardHeader(
                username = username,
                onClick = onClick,
                drawerProgress = drawerProgress
            )
            Box {
                // Pass the navController to NavGraph
                NavGraph(navController = navController)

                Row(
                    modifier = Modifier
                        .padding(vertical = 28.sdp)
                        .offset(y = yOffset.dp)
                        .shadowEffect()
                        .alpha(alpha)
                        .scale(scale.value)
                        .widthIn(max = (LocalConfiguration.current.screenWidthDp - 24).sdp, min = 300.sdp)
                        .height(70.sdp)
                        .align(Alignment.BottomCenter)
                        .clip(RoundedCornerShape(36.sdp))
                        .border(
                            border = BorderStroke(
                                width = 2.sdp,
                                color = colorResource(R.color.white).copy(alpha = 0.2f)
                            ),
                            shape = RoundedCornerShape(36.sdp)
                        )
                        .background(colorResource(R.color.card_elevated))
                        .padding(horizontal = 22.sdp, vertical = 8.sdp),
                    horizontalArrangement = Arrangement.Absolute.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Use a loop to create all navigation items
                    navItems.forEach { (index, itemData) ->
                        NavItem(
                            label = itemData.label,
                            outlinedIconRes = itemData.outlinedIcon,
                            filledIconRes = itemData.filledIcon,
                            isSelected = currentRoute == itemData.route,
                            onClick = { 
                                // Navigate when tab is clicked
                                navigateToTab(navController, itemData.route)
                            }
                        )
                    }
                }
            }
        }
    }
}

// Helper function for navigating to avoid unnecessary navigation operations
private fun navigateToTab(navController: NavController, route: String) {
    // Only navigate if we're not already on that route
    if (navController.currentBackStackEntry?.destination?.route != route) {
        navController.navigate(route) {
            // Pop up to the start destination of the graph to avoid building up a large stack
            popUpTo(navController.graph.startDestinationId) {
                saveState = true
            }
            // Avoid multiple copies of the same destination when reselecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }
    }
}
