package com.devrachit.ken.presentation.screens.dashboard.compare.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.devrachit.ken.R
import com.devrachit.ken.presentation.screens.dashboard.compare.CompareUiStates
import com.devrachit.ken.ui.theme.TextStyleInter20Lh24Fw700
import com.devrachit.ken.utility.composeUtility.SegmentedProgressIndicator2
import com.devrachit.ken.utility.composeUtility.sdp

enum class ViewMode(val displayName: String, val icon: Int) {
    LIST("LIST", R.drawable.ic_list),
    GRID("GRID", R.drawable.ic_grid),
    HORIZONTAL_PAGER("HORIZONTAL_PAGER", R.drawable.ic_pager)
}

@Composable
fun CompareList(
    modifier : Modifier = Modifier,
    uiState: CompareUiStates,
    onViewProfile: (String) -> Unit = {},
    onCompareWith: (String) -> Unit = {},
    onRemoveUser: (String) -> Unit = {},
    onRefreshUser: (String) -> Unit = {},
    initialViewMode: String = "HORIZONTAL_PAGER",
    onViewModeChanged: (String) -> Unit = {}
){
    var currentViewMode by remember { mutableStateOf(ViewMode.valueOf(initialViewMode)) }
    
    LaunchedEffect(initialViewMode) {
        currentViewMode = ViewMode.valueOf(initialViewMode)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(10.sdp))
            .padding(top=30.sdp, bottom = 16.sdp),
        horizontalAlignment = Alignment.Start
    ) {
        // Header with title and view mode switcher
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.sdp, vertical = 10.sdp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Added Friends",
                color = colorResource(R.color.white),
                style = TextStyleInter20Lh24Fw700()
            )
            
            val iconRotation by animateFloatAsState(
                targetValue = when (currentViewMode) {
                    ViewMode.LIST -> 0f
                    ViewMode.HORIZONTAL_PAGER -> 360f
                    ViewMode.GRID -> 180f
                },
                animationSpec = tween(300),
                label = "icon_rotation"
            )
            
            Icon(
                painter = painterResource(currentViewMode.icon),
                contentDescription = "View Mode: ${currentViewMode.displayName}",
                modifier = Modifier
                    .size(20.sdp)
                    .rotate(iconRotation)
                    .clickable {
                        val nextMode = when (currentViewMode) {
                            ViewMode.LIST -> ViewMode.HORIZONTAL_PAGER
                            ViewMode.HORIZONTAL_PAGER -> ViewMode.GRID
                            ViewMode.GRID -> ViewMode.LIST
                        }
                        currentViewMode = nextMode
                        onViewModeChanged(nextMode.name)
                    },
                tint = colorResource(R.color.white)
            )
        }

        val usernames = uiState.friendsDetails?.keys?.toList() ?: emptyList()

        if (usernames.isNotEmpty()) {
            AnimatedContent(
                targetState = currentViewMode,
                transitionSpec = {
                    getTransitionSpec(initialState, targetState)
                },
                label = "view_mode_transition"
            ) { viewMode ->
                when (viewMode) {
                    ViewMode.LIST -> {
                        FriendsListView(
                            usernames = usernames,
                            uiState = uiState,
                            onViewProfile = onViewProfile,
                            onCompareWith = onCompareWith,
                            onRemoveUser = onRemoveUser,
                            onRefreshUser = onRefreshUser
                        )
                    }
                    ViewMode.GRID -> {
                        FriendsGridView(
                            usernames = usernames,
                            uiState = uiState,
                            onViewProfile = onViewProfile,
                            onCompareWith = onCompareWith,
                            onRemoveUser = onRemoveUser,
                            onRefreshUser = onRefreshUser
                        )
                    }
                    ViewMode.HORIZONTAL_PAGER -> {
                        FriendsHorizontalPagerView(
                            usernames = usernames,
                            uiState = uiState,
                            onViewProfile = onViewProfile,
                            onCompareWith = onCompareWith,
                            onRemoveUser = onRemoveUser,
                            onRefreshUser = onRefreshUser
                        )
                    }
                }
            }
        } else {
            Text(
                text = "No friends data available",
                color = Color.White,
                modifier = Modifier.padding(top = 50.sdp)
            )
        }
    }
}

private fun getTransitionSpec(
    initialState: ViewMode,
    targetState: ViewMode
): ContentTransform {
    val animationDuration = 400
    
    return when {
        // List to Pager - slide right and fade
        initialState == ViewMode.LIST && targetState == ViewMode.HORIZONTAL_PAGER -> {
            (slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(animationDuration)
            ) + fadeIn(animationSpec = tween(animationDuration))) togetherWith
            (slideOutHorizontally(
                targetOffsetX = { -it },
                animationSpec = tween(animationDuration)
            ) + fadeOut(animationSpec = tween(animationDuration)))
        }
        
        // Pager to Grid - scale and fade
        initialState == ViewMode.HORIZONTAL_PAGER && targetState == ViewMode.GRID -> {
            (scaleIn(
                initialScale = 0.8f,
                animationSpec = tween(animationDuration)
            ) + fadeIn(animationSpec = tween(animationDuration))) togetherWith
            (scaleOut(
                targetScale = 1.2f,
                animationSpec = tween(animationDuration)
            ) + fadeOut(animationSpec = tween(animationDuration)))
        }
        
        // Grid to List - slide down and fade
        initialState == ViewMode.GRID && targetState == ViewMode.LIST -> {
            (slideInHorizontally(
                initialOffsetX = { -it },
                animationSpec = tween(animationDuration)
            ) + fadeIn(animationSpec = tween(animationDuration))) togetherWith
            (slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(animationDuration)
            ) + fadeOut(animationSpec = tween(animationDuration)))
        }
        
        // Reverse transitions
        initialState == ViewMode.HORIZONTAL_PAGER && targetState == ViewMode.LIST -> {
            (slideInHorizontally(
                initialOffsetX = { -it },
                animationSpec = tween(animationDuration)
            ) + fadeIn(animationSpec = tween(animationDuration))) togetherWith
            (slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(animationDuration)
            ) + fadeOut(animationSpec = tween(animationDuration)))
        }
        
        initialState == ViewMode.GRID && targetState == ViewMode.HORIZONTAL_PAGER -> {
            (scaleIn(
                initialScale = 1.2f,
                animationSpec = tween(animationDuration)
            ) + fadeIn(animationSpec = tween(animationDuration))) togetherWith
            (scaleOut(
                targetScale = 0.8f,
                animationSpec = tween(animationDuration)
            ) + fadeOut(animationSpec = tween(animationDuration)))
        }
        
        initialState == ViewMode.LIST && targetState == ViewMode.GRID -> {
            (slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(animationDuration)
            ) + fadeIn(animationSpec = tween(animationDuration))) togetherWith
            (slideOutHorizontally(
                targetOffsetX = { -it },
                animationSpec = tween(animationDuration)
            ) + fadeOut(animationSpec = tween(animationDuration)))
        }
        
        // Default fade transition
        else -> {
            fadeIn(animationSpec = tween(animationDuration)) togetherWith
            fadeOut(animationSpec = tween(animationDuration))
        }
    }
}

@Composable
private fun FriendsHorizontalPagerView(
    usernames: List<String>,
    uiState: CompareUiStates,
    onViewProfile: (String) -> Unit,
    onCompareWith: (String) -> Unit,
    onRemoveUser: (String) -> Unit,
    onRefreshUser: (String) -> Unit
) {
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
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
            val questionProgress = uiState.friendsQuestionProgressInfo?.get(username)
            val userCalendar = uiState.userProfileCalender?.get(username)

            // Only show widget if we have the essential data
            if (userInfo != null && questionProgress != null) {
                CompareSinglePersonWidget(
                    username = username,
                    userInfo = userInfo,
                    userQuestionProfile = questionProgress,
                    currentTimestamp = uiState.currentTimestamp ?: (System.currentTimeMillis()
                        .toDouble() / 1000),
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
            modifier = Modifier.padding(top = 10.sdp).fillMaxWidth()
        )
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
