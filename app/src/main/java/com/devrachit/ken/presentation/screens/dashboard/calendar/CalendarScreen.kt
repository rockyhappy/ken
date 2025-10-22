package com.devrachit.ken.presentation.screens.dashboard.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import com.devrachit.ken.R
import com.devrachit.ken.presentation.navigation.Screen
import com.devrachit.ken.ui.theme.TextStyleInter14Lh20Fw400
import com.devrachit.ken.ui.theme.TextStyleInter16Lh24Fw700
import com.devrachit.ken.utility.composeUtility.sdp
import java.time.LocalDate

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onQuestionClick: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    
    val pullRefreshState = rememberPullRefreshState(
        refreshing = uiState.isLoading,
        onRefresh = { viewModel.refresh() }
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(R.color.bg_neutral))
                .statusBarsPadding()
        ) {
            // Header
    //        CalendarScreenHeader(onBackClick = onBackClick)

            when {
                uiState.isLoading && uiState.dailyChallenges.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = colorResource(R.color.blue_normal_500)
                        )
                    }
                }
                uiState.error != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = uiState.error ?: "Unknown error",
                            color = colorResource(R.color.white).copy(alpha = 0.7f),
                            style = TextStyleInter14Lh20Fw400()
                        )
                    }
                }
                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .pullRefresh(pullRefreshState)
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 16.sdp, vertical = 12.sdp)
                    ) {
                        // Problem of the Day Card
                        val todayChallengePair = uiState.dailyChallenges.entries
                            .filter { it.key <= uiState.currentDate }
                            .maxByOrNull { it.key }
                        
                        todayChallengePair?.let { (date, challenge) ->
                            ProblemOfTheDayCard(
                                challenge = challenge,
                                date = date,
                                onQuestionClick = onQuestionClick,
                                modifier = Modifier.padding(bottom = 16.sdp)
                            )
                        }
                        
//                        // Stats Card
//                        if (uiState.dailyChallenges.isNotEmpty()) {
//                            ChallengeStatsCard(
//                                dailyChallenges = uiState.dailyChallenges,
//                                displayedMonth = uiState.displayedMonth,
//                                modifier = Modifier.padding(bottom = 16.sdp)
//                            )
//                        }
                        
                        // Calendar Component
                        CalendarComponent(
                            viewModel = viewModel,
                            onQuestionClick = { slug ->
                                onQuestionClick(slug)
                            }
                        )

                        Spacer(modifier = Modifier.height(16.sdp))

                        // Instructions or additional info
                        CalendarInfo()
                    }
                }
            }
        }
        
        // Pull to refresh indicator
        PullRefreshIndicator(
            refreshing = uiState.isLoading,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
            backgroundColor = colorResource(R.color.card_elevated),
            contentColor = colorResource(R.color.white)
        )
    }
}

@Composable
private fun CalendarScreenHeader(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(R.color.bg_neutral))
            .padding(horizontal = 12.sdp, vertical = 12.sdp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier.size(40.sdp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = colorResource(R.color.white),
                modifier = Modifier.size(24.sdp)
            )
        }

        Spacer(modifier = Modifier.width(8.sdp))

        Text(
            text = "Question Calendar",
            color = colorResource(R.color.white),
            style = TextStyleInter16Lh24Fw700(),
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun CalendarInfo() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.sdp)
    ) {
        Text(
            text = "Calendar Guide",
            color = colorResource(R.color.white),
            style = TextStyleInter16Lh24Fw700(),
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.sdp))

        CalendarLegendItem(
            color = colorResource(R.color.blue_normal_500),
            text = "Today's date"
        )

        Spacer(modifier = Modifier.height(4.sdp))

        CalendarLegendItem(
            color = colorResource(R.color.green_normal_500),
            text = "Date with assigned question (click to view)"
        )

        Spacer(modifier = Modifier.height(4.sdp))

        Text(
            text = "• Navigate between months using arrow buttons",
            color = colorResource(R.color.white).copy(alpha = 0.7f),
            style = TextStyleInter14Lh20Fw400()
        )
    }
}

@Composable
private fun CalendarLegendItem(color: androidx.compose.ui.graphics.Color, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 2.sdp)
    ) {
        Box(
            modifier = Modifier
                .size(12.sdp)
                .background(color.copy(alpha = 0.3f), shape = androidx.compose.foundation.shape.CircleShape)
        )

        Spacer(modifier = Modifier.width(8.sdp))

        Text(
            text = text,
            color = colorResource(R.color.white).copy(alpha = 0.7f),
            style = TextStyleInter14Lh20Fw400()
        )
    }
}
