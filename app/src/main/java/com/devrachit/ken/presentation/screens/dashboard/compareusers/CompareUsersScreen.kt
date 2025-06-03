package com.devrachit.ken.presentation.screens.dashboard.compareusers

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.devrachit.ken.R
import com.devrachit.ken.presentation.screens.dashboard.Widgets.DashboardHeaderDetails
import com.devrachit.ken.presentation.screens.dashboard.Widgets.HeatmapCard
import com.devrachit.ken.presentation.screens.dashboard.compare.components.createArcBitmap
import com.devrachit.ken.presentation.screens.dashboard.compareusers.components.ComparisonChart
import com.devrachit.ken.presentation.screens.dashboard.compareusers.components.ComparisonProgressGraph
import com.devrachit.ken.presentation.screens.dashboard.compareusers.components.StreakActivityGraph
import com.devrachit.ken.presentation.screens.dashboard.compareusers.components.UserDropdownSelector
import com.devrachit.ken.utility.composeUtility.HomeScreenShimmer
import com.devrachit.ken.utility.composeUtility.sdp

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun CompareUsersScreen(
    uiState: CompareUsersUiState,
    onInitialize: (String?, String?, List<String>) -> Unit,
    onUser1Selected: (String) -> Unit,
    onUser2Selected: (String) -> Unit,
    onBackPress: () -> Unit,
    username1: String?,
    username2: String?,
    availableUsers: List<String>
) {
    val scrollState = rememberScrollState()
    val isCollapsed =
        scrollState.value > 150 // Collapse when scrolled more than 150dp (reduced from 200dp)

    val pullRefreshState = rememberPullRefreshState(
        refreshing = uiState.isLoading,
        onRefresh = {
            // Only refresh data, don't reset user selections
            if (uiState.username1 != null && uiState.username2 != null) {
                onInitialize(uiState.username1, uiState.username2, availableUsers)
            }
        }
    )

    // Initialize on first load only if no users are selected
    LaunchedEffect(availableUsers.size) {
        if (uiState.username1 == null && uiState.username2 == null && availableUsers.isNotEmpty()) {
            onInitialize(username1, username2, availableUsers)
        }
    }

    BackHandler {
        onBackPress()
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .systemBarsPadding()) {

        // Main scrollable content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
                .background(color = colorResource(R.color.bg_neutral))
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            DashboardHeaderDetails(
                username = "Compare Users",
                onClick = { onBackPress() },
                drawerProgress = 0f
            )

            // Add padding to account for floating card
            val topPadding by animateDpAsState(
                targetValue = if (isCollapsed) 80.sdp else 160.sdp,
                animationSpec = tween(durationMillis = 300)
            )
            Spacer(modifier = Modifier.height(160.sdp))

            // Comparison Content
            if (uiState.user1Data != null && uiState.user2Data != null) {
                // Question Progress Comparison
                ComparisonSection(
                    title = "Question Progress Comparison",
                    modifier = Modifier.padding(top = 20.sdp, start = 18.sdp, end = 18.sdp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        val arcBitmap = createArcBitmap(
                            solved = uiState.user1Data.questionProgress.solved,
                            total = uiState.user1Data.questionProgress.total,
                            easyTotalCount = uiState.user1Data.questionProgress.easyTotalCount,
                            easySolvedCount = uiState.user1Data.questionProgress.easySolvedCount,
                            mediumTotalCount = uiState.user1Data.questionProgress.mediumTotalCount,
                            mediumSolvedCount = uiState.user1Data.questionProgress.mediumSolvedCount,
                            hardTotalCount = uiState.user1Data.questionProgress.hardTotalCount,
                            hardSolvedCount = uiState.user1Data.questionProgress.hardSolvedCount
                        )
                        Image(
                            bitmap = arcBitmap.asImageBitmap(),
                            contentDescription = "Progress Arc",
                            modifier = Modifier
                                .padding(top = 20.sdp)
                                .size(140.sdp)
                        )
                        val arcBitmap2 = createArcBitmap(
                            solved = uiState.user2Data.questionProgress.solved,
                            total = uiState.user2Data.questionProgress.total,
                            easyTotalCount = uiState.user2Data.questionProgress.easyTotalCount,
                            easySolvedCount = uiState.user2Data.questionProgress.easySolvedCount,
                            mediumTotalCount = uiState.user2Data.questionProgress.mediumTotalCount,
                            mediumSolvedCount = uiState.user2Data.questionProgress.mediumSolvedCount,
                            hardTotalCount = uiState.user2Data.questionProgress.hardTotalCount,
                            hardSolvedCount = uiState.user2Data.questionProgress.hardSolvedCount
                        )
                        Image(
                            bitmap = arcBitmap2.asImageBitmap(),
                            contentDescription = "Progress Arc",
                            modifier = Modifier
                                .padding(top = 20.sdp)
                                .size(140.sdp)
                        )

                    }
                }

                // Progress Graphs Comparison
                ComparisonSection(
                    title = "Detailed Progress Comparison",
                    modifier = Modifier.padding(top = 20.sdp, start = 18.sdp, end = 18.sdp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.sdp)
                    ) {
                        // Easy Questions Graph
                        ComparisonProgressGraph(
                            title = "Easy Questions",
                            user1Name = uiState.username1 ?: "User 1",
                            user1Value = uiState.user1Data?.questionProgress?.easySolvedCount ?: 0,
                            user1Total = uiState.user1Data?.questionProgress?.easyTotalCount ?: 0,
                            user2Name = uiState.username2 ?: "User 2",
                            user2Value = uiState.user2Data?.questionProgress?.easySolvedCount ?: 0,
                            user2Total = uiState.user2Data?.questionProgress?.easyTotalCount ?: 0,
                            color1 = colorResource(R.color.easy_filled_blue),
                            color2 = colorResource(R.color.medium_filled_yellow)
                        )

                        // Medium Questions Graph
                        ComparisonProgressGraph(
                            title = "Medium Questions",
                            user1Name = uiState.username1 ?: "User 1",
                            user1Value = uiState.user1Data?.questionProgress?.mediumSolvedCount
                                ?: 0,
                            user1Total = uiState.user1Data?.questionProgress?.mediumTotalCount ?: 0,
                            user2Name = uiState.username2 ?: "User 2",
                            user2Value = uiState.user2Data?.questionProgress?.mediumSolvedCount
                                ?: 0,
                            user2Total = uiState.user2Data?.questionProgress?.mediumTotalCount ?: 0,
                            color1 = colorResource(R.color.easy_filled_blue),
                            color2 = colorResource(R.color.medium_filled_yellow)
                        )

                        // Hard Questions Graph
                        ComparisonProgressGraph(
                            title = "Hard Questions",
                            user1Name = uiState.username1 ?: "User 1",
                            user1Value = uiState.user1Data?.questionProgress?.hardSolvedCount ?: 0,
                            user1Total = uiState.user1Data?.questionProgress?.hardTotalCount ?: 0,
                            user2Name = uiState.username2 ?: "User 2",
                            user2Value = uiState.user2Data?.questionProgress?.hardSolvedCount ?: 0,
                            user2Total = uiState.user2Data?.questionProgress?.hardTotalCount ?: 0,
                            color1 = colorResource(R.color.easy_filled_blue),
                            color2 = colorResource(R.color.medium_filled_yellow)
                        )
                    }
                }

                // Comparison Chart
                ComparisonSection(
                    title = "Progress Comparison Chart",
                    modifier = Modifier.padding(top = 20.sdp, start = 18.sdp, end = 18.sdp)
                ) {
                    ComparisonChart(
                        user1Data = uiState.user1Data,
                        user2Data = uiState.user2Data,
                        user1Name = uiState.username1 ?: "User 1",
                        user2Name = uiState.username2 ?: "User 2"
                    )
                }

                // Streak Activity Graph
                ComparisonSection(
                    title = "Streak & Activity Comparison",
                    modifier = Modifier.padding(top = 20.sdp, start = 18.sdp, end = 18.sdp)
                ) {
                    StreakActivityGraph(
                        user1Name = uiState.username1 ?: "User 1",
                        user1Calendar = uiState.user1Data?.calendarData,
                        user2Name = uiState.username2 ?: "User 2",
                        user2Calendar = uiState.user2Data?.calendarData,
                        color1 = colorResource(R.color.easy_filled_blue),
                        color2 = colorResource(R.color.medium_filled_yellow)
                    )
                }

                // Calendar Comparison (if Android 14+)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    ComparisonSection(
                        title = "Activity Calendar Comparison",
                        modifier = Modifier.padding(top = 20.sdp, start = 18.sdp, end = 18.sdp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.sdp)
                        ) {
                            // User 1 Heatmap
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = uiState.username1 ?: "User 1",
                                    style = androidx.compose.ui.text.TextStyle(
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.White
                                    ),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 8.sdp)
                                )

                                uiState.user1Data.calendarData?.let { calendar ->
                                    HeatmapCard(
                                        modifier = Modifier.fillMaxWidth(),
                                        currentTimestamp = System.currentTimeMillis() / 1000.0,
                                        calenderDetails = calendar.submissionCalendar,
                                        activeYears = calendar.activeYears,
                                        streak = calendar.streak,
                                        activeDays = calendar.totalActiveDays
                                    )
                                } ?: run {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(200.sdp)
                                            .background(
                                                colorResource(R.color.bg_neutral),
                                                RoundedCornerShape(10.sdp)
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "No data available",
                                            color = Color.White.copy(alpha = 0.6f),
                                            fontSize = 12.sp
                                        )
                                    }
                                }
                            }

                            // User 2 Heatmap
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = uiState.username2 ?: "User 2",
                                    style = androidx.compose.ui.text.TextStyle(
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.White
                                    ),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 8.sdp)
                                )

                                uiState.user2Data.calendarData?.let { calendar ->
                                    HeatmapCard(
                                        modifier = Modifier.fillMaxWidth(),
                                        currentTimestamp = System.currentTimeMillis() / 1000.0,
                                        calenderDetails = calendar.submissionCalendar,
                                        activeYears = calendar.activeYears,
                                        streak = calendar.streak,
                                        activeDays = calendar.totalActiveDays
                                    )
                                } ?: run {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(200.sdp)
                                            .background(
                                                colorResource(R.color.bg_neutral),
                                                RoundedCornerShape(10.sdp)
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "No data available",
                                            color = Color.White.copy(alpha = 0.6f),
                                            fontSize = 12.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Activity Statistics Comparison
                ComparisonSection(
                    title = "Activity Statistics",
                    modifier = Modifier.padding(top = 20.sdp, start = 18.sdp, end = 18.sdp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.sdp)
                    ) {
                        // User 1 Stats
                        ActivityStatsCard(
                            modifier = Modifier.weight(1f),
                            username = uiState.username1 ?: "User 1",
                            calendarData = uiState.user1Data.calendarData,
                            color = colorResource(R.color.easy_filled_blue)
                        )

                        // User 2 Stats
                        ActivityStatsCard(
                            modifier = Modifier.weight(1f),
                            username = uiState.username2 ?: "User 2",
                            calendarData = uiState.user2Data.calendarData,
                            color = colorResource(R.color.medium_filled_yellow)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(100.sdp))
            } else if (uiState.isLoading) {
                HomeScreenShimmer()
            }
        }

        // Floating User Selection Card
        FloatingUserSelectionCard(
            modifier = Modifier
                .align(Alignment.TopCenter),
            isCollapsed = isCollapsed,
            uiState = uiState,
            availableUsers = availableUsers,
            onUser1Selected = onUser1Selected,
            onUser2Selected = onUser2Selected
        )

        PullRefreshIndicator(
            refreshing = uiState.isLoading,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
            backgroundColor = colorResource(id = R.color.card_elevated),
            contentColor = colorResource(id = R.color.white)
        )
    }
}

@Composable
private fun FloatingUserSelectionCard(
    modifier: Modifier = Modifier,
    isCollapsed: Boolean,
    uiState: CompareUsersUiState,
    availableUsers: List<String>,
    onUser1Selected: (String) -> Unit,
    onUser2Selected: (String) -> Unit
) {
    val cardTopPadding by animateDpAsState(
        targetValue = if (!isCollapsed) 80.sdp else 16.sdp,
        animationSpec = tween(durationMillis = 300)
    )

    androidx.compose.animation.AnimatedVisibility(
        visible = true,
        modifier = modifier.padding(top = cardTopPadding),
        enter = androidx.compose.animation.fadeIn() + androidx.compose.animation.slideInVertically(),
        exit = androidx.compose.animation.fadeOut() + androidx.compose.animation.slideOutVertically()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.sdp)
                .animateContentSize(
                    animationSpec = tween(
                        durationMillis = 700,
                        easing = androidx.compose.animation.core.FastOutSlowInEasing,
                        delayMillis = 100
                    )
                ),
            colors = CardDefaults.cardColors(
                containerColor = colorResource(id = R.color.card_elevated)
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.sdp
            ),
            shape = RoundedCornerShape(16.sdp)
        ) {
            Column(
                modifier = Modifier.padding(16.sdp)
            ) {
                if (!isCollapsed) {
                    Text(
                        text = "Select Users to Compare",
                        style = androidx.compose.ui.text.TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        ),
                        modifier = Modifier.padding(bottom = 16.sdp)
                    )

                    // Debug info - show number of available users
                    if (availableUsers.isNotEmpty()) {
                        Text(
                            text = "${availableUsers.size} users available",
                            style = androidx.compose.ui.text.TextStyle(
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.7f)
                            ),
                            modifier = Modifier.padding(bottom = 8.sdp)
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.sdp)
                ) {
                    UserDropdownSelector(
                        modifier = Modifier.weight(1f),
                        label = if (isCollapsed) "" else "User 1",
                        selectedUser = uiState.username1,
                        availableUsers = availableUsers,
                        excludeUser = uiState.username2,
                        onUserSelected = onUser1Selected,
                        userData = uiState.user1Data
                    )

                    UserDropdownSelector(
                        modifier = Modifier.weight(1f),
                        label = if (isCollapsed) "" else "User 2",
                        selectedUser = uiState.username2,
                        availableUsers = availableUsers,
                        excludeUser = uiState.username1,
                        onUserSelected = onUser2Selected,
                        userData = uiState.user2Data
                    )
                }
            }
        }
    }
}

@Composable
private fun ComparisonSection(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
//    Card(
//        modifier = modifier.fillMaxWidth(),
//        colors = CardDefaults.cardColors(
//            containerColor = colorResource(id = R.color.card_elevated)
//        )
//    ) {
    Column(
        modifier = modifier
            .padding(16.sdp)
            .border(
                BorderStroke(
                    width = 2.sdp,
                    color = colorResource(R.color.card_elevated)
                ),
                shape = RoundedCornerShape(36.sdp)
            )
    ) {
        Text(
            text = title,
            style = androidx.compose.ui.text.TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            ),
            modifier = Modifier.padding(top=26.sdp,start=24.sdp,bottom = 16.sdp)
        )
        content()
    }
//    }
}

@Composable
fun ActivityStatsCard(
    modifier: Modifier = Modifier,
    username: String,
    calendarData: com.devrachit.ken.domain.models.UserCalendar?,
    color: Color
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(120.sdp),
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(10.sdp)
    ) {
        Column(
            modifier = Modifier.padding(16.sdp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = username,
                style = androidx.compose.ui.text.TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )

            calendarData?.let { data ->
                Column {
                    // Parse submission calendar to get total submissions
                    val totalSubmissions = try {
                        data.getSubmissionCalendarMap().values.sum()
                    } catch (e: Exception) {
                        0
                    }

                    Text(
                        text = "Total Submissions: $totalSubmissions",
                        style = androidx.compose.ui.text.TextStyle(
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    )

                    Text(
                        text = "Streak: ${data.streak} days",
                        style = androidx.compose.ui.text.TextStyle(
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    )

                    Text(
                        text = "Active Days: ${data.totalActiveDays}",
                        style = androidx.compose.ui.text.TextStyle(
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    )
                }
            } ?: run {
                Text(
                    text = "No data available",
                    style = androidx.compose.ui.text.TextStyle(
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.6f)
                    )
                )
            }
        }
    }
}
