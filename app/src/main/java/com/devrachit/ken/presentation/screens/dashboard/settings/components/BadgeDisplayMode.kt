package com.devrachit.ken.presentation.screens.dashboard.settings.components

import com.devrachit.ken.R

enum class BadgeDisplayMode(val displayName: String, val icon: Int, val description: String) {
    DIALOG("Dialog View", R.drawable.ic_sheets_filled, "Show badges in a dialog popup"),
    CAROUSEL("Carousel View", R.drawable.ic_pager, "Show badges in a circular carousel")
}
