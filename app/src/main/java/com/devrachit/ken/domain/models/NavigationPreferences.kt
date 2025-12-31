package com.devrachit.ken.domain.models

data class NavigationPreferences(
    val bottomNavItems: List<String> = NavigationItems.DEFAULT_BOTTOM_NAV,
    val sideNavItems: List<String> = NavigationItems.DEFAULT_SIDE_NAV
)

object NavigationItems {
    const val HOME = "Home"
    const val FRIENDS = "Friends"
    const val QUESTIONS = "Questions"
    const val TRACK = "Track"
    const val COMPARE = "Compare"
    const val SHEETS = "Sheets"
    const val SETTINGS = "Settings"
    const val LOGOUT = "Logout"

    val ALL_ITEMS = listOf(HOME, FRIENDS, QUESTIONS, TRACK, COMPARE, SHEETS, LOGOUT)
    val DEFAULT_BOTTOM_NAV = listOf(HOME, FRIENDS, QUESTIONS, TRACK, COMPARE)
    val DEFAULT_SIDE_NAV = listOf(HOME, FRIENDS, QUESTIONS, TRACK, COMPARE, SHEETS, SETTINGS, LOGOUT)
}
