package com.devrachit.ken.presentation.screens.dashboard.settings.components

import com.devrachit.ken.R

enum class QuestionDetailsViewMode(val displayName: String, val icon: Int, val description: String) {
    PAGER("Pager View", R.drawable.ic_pager, "Organized tabs for sections"),
    SIMPLE("Simple Scrollable", R.drawable.ic_list, "Clean single-scroll view"),
    WEBVIEW("WebView", R.drawable.ic_open_in_browser, "Native LeetCode interface")
}
