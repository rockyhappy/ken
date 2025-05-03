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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    uiState: HomeUiStates,
    onFirstLoad: () -> Unit = {},
) {
    val (hasInitiallyLoaded, setHasInitiallyLoaded) = rememberSaveable { mutableStateOf(false) }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = uiState.isLoading,
        onRefresh = {
            onFirstLoad.invoke()
        }
    )

    LaunchedEffect(true) {
        if (!hasInitiallyLoaded) {
            onFirstLoad.invoke()
            setHasInitiallyLoaded(true)
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
        PullRefreshIndicator(
            refreshing = uiState.isLoading,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
            backgroundColor = colorResource(id = R.color.card_elevated),
            contentColor = colorResource(id = R.color.white)
        )
    }
}