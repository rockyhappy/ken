package com.devrachit.ken.presentation.screens.dashboard.userdetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.devrachit.ken.R
import com.devrachit.ken.presentation.screens.dashboard.Widgets.BadgesWidget
import com.devrachit.ken.presentation.screens.dashboard.Widgets.ContestHistogram
import com.devrachit.ken.presentation.screens.dashboard.Widgets.HeatmapCard
import com.devrachit.ken.presentation.screens.dashboard.Widgets.QuestionProgressCard
import com.devrachit.ken.presentation.screens.dashboard.Widgets.RecentSubmissionCard
import com.devrachit.ken.presentation.screens.dashboard.home.QuestionProgressUiState
import com.devrachit.ken.utility.composeUtility.HomeScreenShimmer
import com.devrachit.ken.utility.composeUtility.sdp
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Menu
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.devrachit.ken.presentation.screens.dashboard.Widgets.DashboardHeader
import com.devrachit.ken.presentation.screens.dashboard.Widgets.DashboardHeaderDetails

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun UserDetailsScreen(
    uiState: UserDetailsUiStates,
    onRefresh: () -> Unit,
    onBackPress: () -> Unit,
    onDeleteUser: (String) -> Unit = {}
) {
    val pullRefreshState = rememberPullRefreshState(
        refreshing = uiState.isLoading,
        onRefresh = onRefresh
    )

    BackHandler {
        onBackPress()
    }

    Box(modifier = Modifier.fillMaxSize().systemBarsPadding()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
                .background(color = colorResource(R.color.bg_neutral))
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            DashboardHeaderDetails(
                username = uiState.username,
                onClick = { onBackPress() },
                drawerProgress = 0f
            )

            // Username Header Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.sdp, start = 18.sdp, end = 18.sdp),
                colors = CardDefaults.cardColors(
                    containerColor = colorResource(id = R.color.card_elevated)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.sdp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = uiState.userProfile?.profile?.userAvatar,
                        placeholder = painterResource(R.drawable.profile_placeholder),
                        contentDescription = "Profile picture",
                        modifier = Modifier
                            .size(50.sdp)
                            .clip(RoundedCornerShape(12.sdp))
                    )
                    
                    Column(
                        modifier = Modifier
                            .padding(start = 16.sdp)
                            .weight(1f),
                        verticalArrangement = Arrangement.Center
                    ) {
                        // Primary text (Real name or username)
                        Text(
                            text = uiState.userProfile?.profile?.realName?.takeIf { it.isNotBlank() }
                                ?: uiState.username
                                ?: "Unknown User",
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )

                        // Secondary text (Username if real name exists, or company)
                        val secondaryText = when {
                            uiState.userProfile?.profile?.realName?.isNotBlank() == true && uiState.username?.isNotBlank() == true ->
                                "@${uiState.username}"

                            uiState.userProfile?.profile?.company?.isNotBlank() == true ->
                                uiState.userProfile?.profile?.company

                            else -> null
                        }

                        secondaryText?.let { text ->
                            Text(
                                text = text,
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    color = Color.White.copy(alpha = 0.7f)
                                ),
                                modifier = Modifier.padding(top = 4.sdp)
                            )
                        }
                    }

                    // Delete icon
                    IconButton(
                        onClick = { onDeleteUser(uiState.username ?: "") },
                        modifier = Modifier.padding(end = 4.sdp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_delete_outline),
                            contentDescription = "Delete user",
                            tint = Color.White
                        )
                    }
                }
            }

            // Convert UserDetails question progress to Home question progress for compatibility
            val homeQuestionProgress = QuestionProgressUiState(
                solved = uiState.questionProgress.solved,
                attempting = uiState.questionProgress.attempting,
                total = uiState.questionProgress.total,
                easyTotalCount = uiState.questionProgress.easyTotalCount,
                easySolvedCount = uiState.questionProgress.easySolvedCount,
                mediumTotalCount = uiState.questionProgress.mediumTotalCount,
                mediumSolvedCount = uiState.questionProgress.mediumSolvedCount,
                hardTotalCount = uiState.questionProgress.hardTotalCount,
                hardSolvedCount = uiState.questionProgress.hardSolvedCount
            )

            // Use the QuestionProgressCard widget
            QuestionProgressCard(
                questionProgress = homeQuestionProgress,
                modifier = Modifier.padding(top = 20.sdp, start = 18.sdp, end = 18.sdp)
            )

            when {
                uiState.currentTimestamp != null -> {
                    HeatmapCard(
                        modifier = Modifier.padding(top = 20.sdp, start = 18.sdp, end = 18.sdp),
                        currentTimestamp = uiState.currentTimestamp!!,
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
                        contestRatingHistogramResponse = uiState.contestRatingHistogramResponse!!,
                        userContestRankingResponse = uiState.userContestRankingResponse!!,
                        modifier = Modifier.padding(top = 20.sdp, start = 18.sdp, end = 18.sdp),
                    )
                }
                !uiState.userParticipationInAnyContest -> {
                    // User has no contest participation
                }
                else -> HomeScreenShimmer()
            }

            when {
                uiState.userBadgesResponse != null && (uiState.userBadgesResponse!!.data?.matchedUser?.badges?.size != 0) -> {
                    BadgesWidget(
                        modifier = Modifier.padding(
                            top = 20.sdp,
                            start = 18.sdp,
                            end = 18.sdp
                        ),
                        userBadgesResponse = uiState.userBadgesResponse!!
                    )
                }
                uiState.userBadgesResponse != null && (uiState.userBadgesResponse!!.data?.matchedUser?.badges?.size) == 0 -> {
                    // User has no badges
                }
                else -> HomeScreenShimmer()
            }

            when {
                uiState.recentSubmissions != null -> {
                    RecentSubmissionCard(
                        data = uiState.recentSubmissions!!,
                        modifier = Modifier.padding(
                            top = 20.sdp,
                            start = 18.sdp,
                            end = 18.sdp,
                            bottom = 0.sdp
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
