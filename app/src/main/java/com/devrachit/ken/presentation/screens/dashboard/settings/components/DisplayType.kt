package com.devrachit.ken.presentation.screens.dashboard.settings.components

import com.devrachit.ken.R

enum class DisplayType(val displayName: String, val icon: Int, val description: String) {
    LIST("List View", R.drawable.ic_list, "Display friends in a vertical list"),
    GRID("Grid View", R.drawable.ic_grid, "Display friends in a grid layout"),
    HORIZONTAL_PAGER("Pager View", R.drawable.ic_pager, "Swipe through friends horizontally")
}
