package com.devrachit.ken.presentation.screens.dashboard.home

import android.util.Log
import com.devrachit.ken.domain.models.ContestRatingHistogramResponse
import com.devrachit.ken.domain.models.UserBadgesResponse
import com.devrachit.ken.domain.models.UserCalendar
import com.devrachit.ken.domain.models.UserContestRankingResponse
import com.devrachit.ken.domain.models.UserRecentAcSubmissionResponse

data class HomeUiStates(
    val isLoading: Boolean = false,
    val questionProgress: QuestionProgressUiState = QuestionProgressUiState(),
    val userProfileCalender: UserCalendar? = null,
    val currentTimestamp: Double? = null,
    val recentSubmissions: UserRecentAcSubmissionResponse? = null,
    val contestRatingHistogramResponse : ContestRatingHistogramResponse?=null,
    val userContestRankingResponse: UserContestRankingResponse? = null,
    val userBadgesResponse: UserBadgesResponse? = null,
)

data class QuestionProgressUiState(
    val solved: Int = 0,
    val attempting: Int = 0,
    val total: Int = 3521,
    val easyTotalCount: Int = 873,
    val easySolvedCount: Int = 0,
    val mediumTotalCount: Int = 1826,
    val mediumSolvedCount: Int = 0,
    val hardTotalCount: Int = 822,
    val hardSolvedCount: Int = 0,
)


data class LoadingStates(
    var questionStatusLoading: Boolean = false,
    var currentTimeLoading: Boolean = false,
    var calendarLoading: Boolean = false,
    var submissionsLoading: Boolean = false,
    var badgesLoading: Boolean = false,
    var contestRankingLoading: Boolean = false,
    var contestRankingHistogramLoading: Boolean = false,
    var pullToRefreshLoading: Boolean = questionStatusLoading || currentTimeLoading || calendarLoading || submissionsLoading || badgesLoading || contestRankingLoading || contestRankingHistogramLoading
)
