package com.devrachit.ken.presentation.screens.dashboard.home

import com.devrachit.ken.domain.models.UserCalendar
import com.devrachit.ken.domain.models.UserRecentAcSubmissionResponse

data class HomeUiStates(
    val isLoading: Boolean = false,
    val questionProgress: QuestionProgressUiState = QuestionProgressUiState(),
    val userProfileCalender :UserCalendar? = null,
    val currentTimestamp: Double?=null,
    val recentSubmissions :UserRecentAcSubmissionResponse ? = null
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

