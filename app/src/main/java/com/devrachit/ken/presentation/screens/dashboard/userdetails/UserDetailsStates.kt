package com.devrachit.ken.presentation.screens.dashboard.userdetails

import com.devrachit.ken.domain.models.ContestRatingHistogramResponse
import com.devrachit.ken.domain.models.LeetCodeUserInfo
import com.devrachit.ken.domain.models.UserBadgesResponse
import com.devrachit.ken.domain.models.UserCalendar
import com.devrachit.ken.domain.models.UserContestRankingResponse
import com.devrachit.ken.domain.models.UserRecentAcSubmissionResponse

data class UserDetailsUiStates(
    val isLoading: Boolean = false,
    val username: String = "",
    val userProfile: LeetCodeUserInfo? = null,
    val questionProgress: UserDetailsQuestionProgressUiState = UserDetailsQuestionProgressUiState(),
    val userProfileCalender: UserCalendar? = null,
    val currentTimestamp: Double? = null,
    val recentSubmissions: UserRecentAcSubmissionResponse? = null,
    val contestRatingHistogramResponse: ContestRatingHistogramResponse? = null,
    val userContestRankingResponse: UserContestRankingResponse? = null,
    val userBadgesResponse: UserBadgesResponse? = null,
    var userParticipationInAnyContest: Boolean = true,
)

data class UserDetailsQuestionProgressUiState(
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

data class UserDetailsLoadingStates(
    var questionStatusLoading: Boolean = false,
    var currentTimeLoading: Boolean = false,
    var calendarLoading: Boolean = false,
    var submissionsLoading: Boolean = false,
    var badgesLoading: Boolean = false,
    var contestRankingLoading: Boolean = false,
    var contestRankingHistogramLoading: Boolean = false,
    var pullToRefreshLoading: Boolean = questionStatusLoading || currentTimeLoading || calendarLoading || submissionsLoading || badgesLoading || contestRankingLoading || contestRankingHistogramLoading
)
