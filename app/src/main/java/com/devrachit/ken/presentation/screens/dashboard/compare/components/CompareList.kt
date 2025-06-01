package com.devrachit.ken.presentation.screens.dashboard.compare.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import com.devrachit.ken.R
import com.devrachit.ken.presentation.screens.dashboard.compare.CompareUiStates
import com.devrachit.ken.ui.theme.TextStyleInter20Lh24Fw700
import com.devrachit.ken.utility.composeUtility.sdp

@Composable
fun CompareList(
    modifier : Modifier = Modifier,
    uiState: CompareUiStates,
    onViewProfile: (String) -> Unit = {},
    onCompareWith: (String) -> Unit = {},
    onRemoveUser: (String) -> Unit = {},
    onRefreshUser: (String) -> Unit = {}
){
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(10.sdp))
            .padding(top=30.sdp, bottom = 16.sdp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Compare Friends",
            modifier = Modifier.padding(bottom = 10.sdp, start = 30.sdp),
            color = colorResource(R.color.white),
            style = TextStyleInter20Lh24Fw700()
        )
        // Get all usernames from friendsDetails (this will be our source of truth)
        val usernames = uiState.friendsDetails?.keys?.toList() ?: emptyList()

        if (usernames.isNotEmpty()) {
            val pagerState = if (false && usernames.size >= 3) { //TODO : Change this accordingly considering the UX requirements
                // Circular behavior for 3 or more users
                rememberPagerState(
                    initialPage = Int.MAX_VALUE / 2,
                    pageCount = { Int.MAX_VALUE }
                )
            } else {
                // Linear behavior for 1-2 users
                rememberPagerState(
                    initialPage = 0,
                    pageCount = { usernames.size }
                )
            }
            
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .wrapContentHeight()
                    .padding(vertical = 8.sdp),
                contentPadding = PaddingValues(horizontal = 22.sdp),
                pageSpacing = 8.sdp
            ) { page ->
                val actualIndex = if (usernames.size >= 3) {
                    page % usernames.size
                } else {
                    page
                }
                val username = usernames[actualIndex]
                val userInfo = uiState.friendsDetails?.get(username)
                val questionProgress =
                    uiState.friendsQuestionProgressInfo?.get(username)
                val userCalendar = uiState.userProfileCalender?.get(username)

                // Only show widget if we have the essential data
                if (userInfo != null && questionProgress != null) {
                    CompareSinglePersonWidget(
                        username = username,
                        userInfo = userInfo,
                        userQuestionProfile = questionProgress,
                        currentTimestamp = uiState.currentTimestamp
                            ?: (System.currentTimeMillis().toDouble() / 1000),
                        calenderDetails = userCalendar?.submissionCalendar ?: "{}",
                        activeYears = userCalendar?.activeYears ?: emptyList(),
                        activeDays = userCalendar?.totalActiveDays ?: 0,
                        streak = userCalendar?.streak ?: 0,
                        modifier = Modifier.padding(horizontal = 8.sdp),
                        onViewProfile = onViewProfile,
                        onCompareWith = onCompareWith,
                        onRemoveUser = onRemoveUser,
                        onRefreshUser = onRefreshUser
                    )
                }
            }
            
            // Pager Indicator
            PagerIndicator(
                pageCount = usernames.size,
                currentPage = (pagerState.currentPage % usernames.size),
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 10.sdp)
            )
        } else {
            Text(
                text = "No friends data available",
                color = Color.White,
                modifier = Modifier.padding(top = 50.sdp)
            )
        }
    }
}

@Composable
fun PagerIndicator(
    modifier: Modifier = Modifier,
    pageCount: Int,
    currentPage: Int
) {
    when {
        pageCount<=1 ->{

        }
        pageCount <= 2 -> {
            // Show all dots for small counts
            Row(
                modifier = modifier,
                horizontalArrangement = Arrangement.Center
            ) {
                for (i in 0 until pageCount) {
                    val isActive = i == currentPage
                    val indicatorColor = if (isActive) colorResource(R.color.white).copy(alpha = 0.8f) else colorResource(R.color.white).copy(alpha = 0.2f)
                    val indicatorSize = if (isActive) 16.sdp else 16.sdp

                    Box(
                        modifier = Modifier
                            .height(indicatorSize)
                            .width(indicatorSize)
                            .padding(vertical = 4.sdp, horizontal = 4.sdp)
                            .clip(RoundedCornerShape(50))
                            .background(indicatorColor)
                    )
                }
            }
        }
        pageCount <= 15 -> {
            // Show limited dots with ellipsis for medium counts
            Row(
                modifier = modifier,
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val maxVisibleDots = 5
                val halfVisible = maxVisibleDots / 2
                
                for (i in 0 until maxVisibleDots) {
                    val actualIndex = when {
                        currentPage < halfVisible -> i
                        currentPage >= pageCount - halfVisible -> pageCount - maxVisibleDots + i
                        else -> currentPage - halfVisible + i
                    }
                    
                    if (actualIndex in 0 until pageCount) {
                        val isActive = actualIndex == currentPage
                        val indicatorColor = if (isActive) colorResource(R.color.white).copy(alpha = 0.8f) else colorResource(R.color.white).copy(alpha = 0.2f)
                        val indicatorSize = if (isActive) 16.sdp else 16.sdp

                        Box(
                            modifier = Modifier
                                .height(indicatorSize)
                                .width(indicatorSize)
                                .padding(vertical = 4.sdp, horizontal = 4.sdp)
                                .clip(RoundedCornerShape(50))
                                .background(indicatorColor)
                        )
                    }
                }
            }
        }
        else -> {
            // Show text indicator for large counts
            Row(
                modifier = modifier,
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${currentPage + 1} of $pageCount",
                    color = colorResource(R.color.white).copy(alpha = 0.8f),
                    modifier = Modifier.padding(horizontal = 16.sdp, vertical = 0.sdp)
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewCompareList() {
    // Empty state for preview
    val sampleUiState = CompareUiStates(
        friendsDetails = emptyMap(),
        friendsQuestionProgressInfo = emptyMap(),
        userProfileCalender = emptyMap(),
        currentTimestamp = System.currentTimeMillis().toDouble() / 1000,
        isLoading = false
    )

    CompareList(
        uiState = sampleUiState
    )
}
