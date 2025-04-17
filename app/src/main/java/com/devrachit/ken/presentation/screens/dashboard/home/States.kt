package com.devrachit.ken.presentation.screens.dashboard.home

data class HomeUiStates(
    val isLoading: Boolean = false,
    val questionProgress: QuestionProgressUiState = QuestionProgressUiState()
)

data class QuestionProgressUiState(
    val solved: Int = 0,
    val attempting: Int = 0,
    val total: Int = 3521,
    val easyTotalCount: Int =873,
    val easySolvedCount: Int = 0,
    val mediumTotalCount: Int = 1826,
    val mediumSolvedCount: Int = 0,
    val hardTotalCount: Int = 822,
    val hardSolvedCount: Int = 0,
)

