package com.devrachit.ken.presentation.navigation

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.devrachit.ken.R

/**
 * Data class representing navigation item information
 */
data class NavItemData(
    val label: String,
    @DrawableRes val outlinedIcon: Int,
    @DrawableRes val filledIcon: Int,
    val route: String
)

/**
 * Provides the navigation items map for the bottom navigation
 */
@Composable
fun rememberNavigationItems() = remember {
    mapOf(
        0 to NavItemData(
            "Home",
            R.drawable.ic_home_outlined,
            R.drawable.ic_home_filled,
            Screen.Home.route
        ),
        1 to NavItemData(
            "Questions",
            R.drawable.ic_questions_outlined,
            R.drawable.ic_questions_filled,
            Screen.Questions.route
        ),
        2 to NavItemData(
            "Compare",
            R.drawable.ic_compare_outlined,
            R.drawable.ic_compare_filled,
            Screen.Compare.route
        ),
        3 to NavItemData(
            "Sheets",
            R.drawable.ic_sheets_outlined,
            R.drawable.ic_sheets_filled,
            Screen.Sheets.route
        ),
        4 to NavItemData(
            "Logout",
            R.drawable.ic_logout_outlined,
            R.drawable.ic_logout_filled,
            Screen.Logout.route
        )
    )
}

/**
 * Get the index of a route in the navigation items
 */
fun getRouteIndex(route: String?): Int? {
    if (route == null) return null
    
    return when (route) {
        Screen.Home.route -> 0
        Screen.Questions.route -> 1
        Screen.Compare.route -> 2
        Screen.Sheets.route -> 3
        else -> null
    }
}