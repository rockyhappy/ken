package com.devrachit.ken.presentation.screens.dashboard.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import com.devrachit.ken.R
import com.devrachit.ken.presentation.screens.dashboard.Widgets.HeatmapCard
import com.devrachit.ken.presentation.screens.dashboard.Widgets.QuestionProgressCard
import com.devrachit.ken.presentation.screens.dashboard.Widgets.RecentSubmissionCard
import com.devrachit.ken.utility.composeUtility.HomeScreenShimmer
import com.devrachit.ken.utility.composeUtility.ProfilePictureShimmer
import com.devrachit.ken.utility.composeUtility.sdp
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import com.devrachit.ken.presentation.screens.dashboard.Widgets.BadgesWidget
import com.devrachit.ken.presentation.screens.dashboard.Widgets.ContestHistogram
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    uiState: HomeUiStates,
    onFirstLoad: () -> Unit = {},
) {
    val (hasInitiallyLoaded, setHasInitiallyLoaded) = rememberSaveable { mutableStateOf(false) }
    val firebaseAnalytics = Firebase.analytics

    val pullRefreshState = rememberPullRefreshState(
        refreshing = uiState.isLoading,
        onRefresh = {
            firebaseAnalytics.logEvent("home_screen_refresh") {
                param("has_question_progress", (uiState.questionProgress != null).toString())
                param("has_calendar_data", (uiState.userProfileCalender != null).toString())
                param("has_contest_data", (uiState.contestRatingHistogramResponse != null).toString())
                param("has_badges", (uiState.userBadgesResponse?.data?.matchedUser?.badges?.isNotEmpty() == true).toString())
            }
            onFirstLoad.invoke()
        }
    )

    LaunchedEffect(true) {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "home_screen")
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "HomeScreen")
        }
        
        if (!hasInitiallyLoaded) {
            firebaseAnalytics.logEvent("home_screen_first_load") {
                param("timestamp", System.currentTimeMillis())
            }
            onFirstLoad.invoke()
            setHasInitiallyLoaded(true)
        }
    }

    // Track when data becomes available
    LaunchedEffect(uiState.questionProgress) {
        if (uiState.questionProgress != null) {
            firebaseAnalytics.logEvent("home_question_progress_loaded") {
                param("total_solved", uiState.questionProgress.solved.toLong())
                param("easy_solved", uiState.questionProgress.easySolvedCount.toLong())
                param("medium_solved", uiState.questionProgress.mediumSolvedCount.toLong())
                param("hard_solved", uiState.questionProgress.hardSolvedCount.toLong())
            }
        }
    }

    LaunchedEffect(uiState.userProfileCalender) {
        if (uiState.userProfileCalender != null) {
            firebaseAnalytics.logEvent("home_calendar_data_loaded") {
                param("streak", uiState.userProfileCalender.streak.toLong())
                param("active_days", uiState.userProfileCalender.totalActiveDays.toLong())
                param("active_years_count", uiState.userProfileCalender.activeYears.size.toLong())
            }
        }
    }

    LaunchedEffect(uiState.userBadgesResponse) {
        if (uiState.userBadgesResponse != null) {
            val badgeCount = uiState.userBadgesResponse.data?.matchedUser?.badges?.size ?: 0
            firebaseAnalytics.logEvent("home_badges_loaded") {
                param("badge_count", badgeCount.toLong())
                param("has_badges", (badgeCount > 0).toString())
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
                .background(color = colorResource(R.color.bg_neutral))
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Use the new QuestionProgressCard widget
            QuestionProgressCard(
                questionProgress = uiState.questionProgress,
                modifier = Modifier.padding(top = 20.sdp, start = 18.sdp, end = 18.sdp)
            )
            when {
                uiState.currentTimestamp != null -> {
                    HeatmapCard(
                        modifier = Modifier.padding(top = 20.sdp, start = 18.sdp, end = 18.sdp),
                        currentTimestamp = uiState.currentTimestamp,
                        calenderDetails = uiState.userProfileCalender?.submissionCalendar ?: "",
                        activeYears = uiState.userProfileCalender?.activeYears ?: emptyList(),
                        streak = uiState.userProfileCalender?.streak ?: 0,
                        activeDays = uiState.userProfileCalender?.totalActiveDays ?: 0
                    )
                }

                else -> HomeScreenShimmer()
            }
            when {
                uiState.contestRatingHistogramResponse != null &&
                        uiState.userContestRankingResponse != null &&
                        uiState.userParticipationInAnyContest -> {
                    ContestHistogram(
                        contestRatingHistogramResponse = uiState.contestRatingHistogramResponse,
                        userContestRankingResponse = uiState.userContestRankingResponse,
                        modifier = Modifier.padding(top = 20.sdp, start = 18.sdp, end = 18.sdp),
                    )
                }

                !uiState.userParticipationInAnyContest -> {

                }

                else -> HomeScreenShimmer()
            }
            when {
                uiState.userBadgesResponse != null && (uiState.userBadgesResponse.data?.matchedUser?.badges?.size != 0) -> {
                    BadgesWidget(
                        modifier = Modifier.padding(
                            top = 20.sdp,
                            start = 18.sdp,
                            end = 18.sdp
                        ),
                        userBadgesResponse = uiState.userBadgesResponse
                    )
                }

                uiState.userBadgesResponse != null && (uiState.userBadgesResponse.data?.matchedUser?.badges?.size) == 0 -> {
                    // user has no badge don't need to show any
                }

                else -> HomeScreenShimmer()
            }
            when {
                uiState.recentSubmissions != null -> {
                    RecentSubmissionCard(
                        data = uiState.recentSubmissions,
                        modifier = Modifier.padding(
                            top = 20.sdp,
                            start = 18.sdp,
                            end = 18.sdp,
                            bottom = 20.sdp
                        ),
                        currentTime = uiState.currentTimestamp?.toLong(),
                    )
                }

                else -> HomeScreenShimmer()
            }

        }

//        Text(
//            text=uiState.userBadgesResponse.toString(),
//            color= Color.White,
//        )
        PullRefreshIndicator(
            refreshing = uiState.isLoading,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
            backgroundColor = colorResource(id = R.color.card_elevated),
            contentColor = colorResource(id = R.color.white)
        )
    }

}
